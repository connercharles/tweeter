package edu.byu.cs.tweeter.client.view.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.FeedRequest;
import edu.byu.cs.tweeter.model.service.response.FeedResponse;
import edu.byu.cs.tweeter.client.presenter.FeedPresenter;
import edu.byu.cs.tweeter.client.view.asyncTasks.FeedTask;
import edu.byu.cs.tweeter.client.view.util.ImageUtils;

public class FeedFragment extends Fragment implements FeedPresenter.View{
    private static final String LOG_TAG = "FeedFragment";
    private static final String USER_KEY = "UserKey";
    private static final String AUTH_TOKEN_KEY = "AuthTokenKey";

    private static final int LOADING_DATA_VIEW = 0;
    private static final int ITEM_VIEW = 1;

    private static final int PAGE_SIZE = 10;

    private User user;
    private Status status;
    private AuthToken authToken;
    private FeedPresenter presenter;

    private FeedFragment.FeedRecyclerViewAdapter feedRecyclerViewAdapter;

    public static FeedFragment newInstance(User user, AuthToken authToken) {
        FeedFragment fragment = new FeedFragment();

        Bundle args = new Bundle(2);
        args.putSerializable(USER_KEY, user);
        args.putSerializable(AUTH_TOKEN_KEY, authToken);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        //noinspection ConstantConditions
        user = (User) getArguments().getSerializable(USER_KEY);
        authToken = (AuthToken) getArguments().getSerializable(AUTH_TOKEN_KEY);

        presenter = new FeedPresenter(this);

        RecyclerView feedRecyclerView = view.findViewById(R.id.feedRecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        feedRecyclerView.setLayoutManager(layoutManager);

        feedRecyclerViewAdapter = new FeedFragment.FeedRecyclerViewAdapter();
        feedRecyclerView.setAdapter(feedRecyclerViewAdapter);

        feedRecyclerView.addOnScrollListener(new FeedFragment.FeedRecyclerViewPaginationScrollListener(layoutManager));

        return view;
    }

    private class FeedHolder extends RecyclerView.ViewHolder {

        private final ImageView userImage;
        private final TextView userAlias;
        private final TextView userName;
        private final TextView postTime;
        private final TextView statusMessage;

        FeedHolder(@NonNull View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.statusAuthorImage);
            userAlias = itemView.findViewById(R.id.statusAuthorAlias);
            userName = itemView.findViewById(R.id.statusAuthorName);
            postTime = itemView.findViewById(R.id.statusPostTime);
            statusMessage = itemView.findViewById(R.id.statusMessage);
        }

        void bindStatus(Status status) {
            userImage.setImageDrawable(ImageUtils.drawableFromByteArray(status.getAuthor().getImageBytes()));
            userAlias.setText(status.getAuthor().getAlias());
            userName.setText(status.getAuthor().getName());
            DateTimeFormatter postFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);

            postTime.setText(status.getWhenPosted().format(postFormatter));
            parseMessage(status);
        }

        private void parseMessage(Status status) {
            String message = status.getMessage();
            List<String> containedUrls = new ArrayList<>();
            List<String> containedMentions = new ArrayList<>();
            String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
            Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
            Matcher urlMatcher = pattern.matcher(message);

            while (urlMatcher.find()) {
                containedUrls.add(message.substring(urlMatcher.start(0),
                        urlMatcher.end(0)));
            }

            Matcher mentionMatcher = Pattern.compile("^[@]\\w+|(?<=\\s)[@]\\w+").matcher(message);
            while (mentionMatcher.find()) {
                containedMentions.add(message.substring(mentionMatcher.start(0),
                        mentionMatcher.end(0)));
            }

            SpannableString ss = new SpannableString(message);
            setClickableUrl(status, containedUrls, ss);
            setClickableMention(status, containedMentions, ss);

        }

        private void setClickableUrl(Status status, List<String> containedUrls, SpannableString ss) {
            String message = status.getMessage();
            for (String word : containedUrls) {
                int firstIndex = message.indexOf(word);
                int lastIndex = firstIndex + word.length();

                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View textView) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(word));
                        startActivity(browserIntent);
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setUnderlineText(true);
                    }
                };
                ss.setSpan(clickableSpan, firstIndex, lastIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                statusMessage.setMovementMethod(LinkMovementMethod.getInstance());
                statusMessage.setText(ss);
            }
        }

        private void setClickableMention(Status status, List<String> containedUrls, SpannableString ss) {
            String message = status.getMessage();
            for (String word : containedUrls) {
                int firstIndex = message.indexOf(word);
                int lastIndex = firstIndex + word.length();

                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View textView) {
                        Intent intent = new Intent(getActivity(), UserActivity.class);

                        intent.putExtra(UserActivity.CLICKED_USER, status.getAuthor());
                        intent.putExtra(UserActivity.MAIN_USER, user);
                        intent.putExtra(UserActivity.AUTH_TOKEN_KEY, authToken);

                        startActivity(intent);
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setUnderlineText(true);
                    }
                };
                ss.setSpan(clickableSpan, firstIndex, lastIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                statusMessage.setMovementMethod(LinkMovementMethod.getInstance());
                statusMessage.setText(ss);
            }
        }
    }

    private class FeedRecyclerViewAdapter extends RecyclerView.Adapter<FeedFragment.FeedHolder> implements FeedTask.Observer {

        private final List<Status> statuses = new ArrayList<>();

        private edu.byu.cs.tweeter.model.domain.Status lastStatus;

        private boolean hasMorePages;
        private boolean isLoading = false;

        FeedRecyclerViewAdapter() {
            loadMoreItems();
        }

        void addItems(List<Status> newStatuses) {
            int startInsertPosition = statuses.size();
            statuses.addAll(newStatuses);
            this.notifyItemRangeInserted(startInsertPosition, newStatuses.size());
        }

        void addItem(Status status) {
            statuses.add(status);
            this.notifyItemInserted(statuses.size() - 1);
        }

        void removeItem(Status status) {
            int position = statuses.indexOf(status);
            statuses.remove(position);
            this.notifyItemRemoved(position);
        }

        @NonNull
        @Override
        public FeedFragment.FeedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(FeedFragment.this.getContext());
            View view;

            if(viewType == LOADING_DATA_VIEW) {
                view =layoutInflater.inflate(R.layout.loading_row, parent, false);

            } else {
                view = layoutInflater.inflate(R.layout.status_row, parent, false);
            }

            return new FeedFragment.FeedHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull FeedFragment.FeedHolder feedHolder, int position) {
            if(!isLoading) {
                feedHolder.bindStatus(statuses.get(position));
            }
        }

        @Override
        public int getItemCount() {
            return statuses.size();
        }

        @Override
        public int getItemViewType(int position) {
            return (position == statuses.size() - 1 && isLoading) ? LOADING_DATA_VIEW : ITEM_VIEW;
        }

        void loadMoreItems() {
            isLoading = true;
            addLoadingFooter();

            FeedTask feedTask = new FeedTask(presenter, this);
            FeedRequest request = new FeedRequest(user, PAGE_SIZE, lastStatus);
            feedTask.execute(request);
        }

        @Override
        public void feedRetrieved(FeedResponse feedResponse) {
            List<Status> feed = feedResponse.getFeed();

            lastStatus = (feed.size() > 0) ? feed.get(feed.size() -1) : null;
            hasMorePages = feedResponse.getHasMorePages();

            isLoading = false;
            removeLoadingFooter();
            feedRecyclerViewAdapter.addItems(feed);
        }

        @Override
        public void handleException(Exception exception) {
            Log.e(LOG_TAG, exception.getMessage(), exception);

            if(exception instanceof TweeterRemoteException) {
                TweeterRemoteException remoteException = (TweeterRemoteException) exception;
                Log.e(LOG_TAG, "Remote Exception Type: " + remoteException.getRemoteExceptionType());

                Log.e(LOG_TAG, "Remote Stack Trace:");
                if(remoteException.getRemoteStackTrace() != null) {
                    for(String stackTraceLine : remoteException.getRemoteStackTrace()) {
                        Log.e(LOG_TAG, "\t\t" + stackTraceLine);
                    }
                }
            }

            removeLoadingFooter();
            Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
        }

        private void addLoadingFooter() {
            addItem(new Status("Loading", new User("test", "user", "")));
        }

        private void removeLoadingFooter() {
            removeItem(statuses.get(statuses.size() - 1));
        }
    }

    private class FeedRecyclerViewPaginationScrollListener extends RecyclerView.OnScrollListener {

        private final LinearLayoutManager layoutManager;

        FeedRecyclerViewPaginationScrollListener(LinearLayoutManager layoutManager) {
            this.layoutManager = layoutManager;
        }

        @Override
        public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

            if (!feedRecyclerViewAdapter.isLoading && feedRecyclerViewAdapter.hasMorePages) {
                if ((visibleItemCount + firstVisibleItemPosition) >=
                        totalItemCount && firstVisibleItemPosition >= 0) {
                    feedRecyclerViewAdapter.loadMoreItems();
                }
            }
        }
    }

}
