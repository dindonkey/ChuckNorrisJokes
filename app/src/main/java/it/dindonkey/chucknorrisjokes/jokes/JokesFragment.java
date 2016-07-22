package it.dindonkey.chucknorrisjokes.jokes;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import it.dindonkey.chucknorrisjokes.App;
import it.dindonkey.chucknorrisjokes.R;
import it.dindonkey.chucknorrisjokes.data.Joke;
import it.dindonkey.chucknorrisjokes.events.ReloadJokesEvent;
import it.dindonkey.chucknorrisjokes.events.RxBus;
import it.dindonkey.chucknorrisjokes.jokes.JokesContract.UserActionsListener;
import rx.Subscription;

public class JokesFragment extends Fragment implements JokesContract.View
{
    @Inject
    UserActionsListener mUserActionsListener;

    @Inject
    RxBus mRxBus;

    private JokesAdapter mJokesAdapter;

    private ErrorFragment errorFragment;
    private LoadingFragment loadingFragment;
    private Subscription mReloadEventSubscription;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public static JokesFragment newInstance()
    {
        return new JokesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getApplication().getComponent().inject(this);

        mJokesAdapter = new JokesAdapter(new ArrayList<>(0));
        loadingFragment = LoadingFragment.newInstance();
        errorFragment = ErrorFragment.newInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_jokes, container, false);
        configureJokesRecycleView(rootView);
        configureSwipeRefreshLayout(rootView);

        return rootView;
    }

    private void configureJokesRecycleView(View rootView)
    {
        RecyclerView jokesRecyclerView = (RecyclerView) rootView.findViewById(R.id.jokes_list);
        jokesRecyclerView.setAdapter(mJokesAdapter);
        jokesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
    }

    private void configureSwipeRefreshLayout(View rootView)
    {
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        mSwipeRefreshLayout.setOnRefreshListener(() -> mUserActionsListener.loadJokes(true));
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mUserActionsListener.bindView(this);
        mUserActionsListener.loadJokes(false);
        mReloadEventSubscription = mRxBus.register(ReloadJokesEvent.class,
                r -> mUserActionsListener.loadJokes(true));
    }

    private App getApplication()
    {
        return (App) getActivity().getApplication();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        mUserActionsListener.unBindView();
        mUserActionsListener.clearSubscription();
        if (null != mReloadEventSubscription)
        {
            mReloadEventSubscription.unsubscribe();
        }
    }

    @Override
    public void showJokes(List<Joke> jokes)
    {
        mJokesAdapter.refreshData(jokes);
    }

    @Override
    public void showLoading()
    {
        if (!mSwipeRefreshLayout.isRefreshing())
        {
            replaceTopFragment(loadingFragment);
        }
    }

    @Override
    public void hideLoading()
    {
        removeTopFragment();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showError()
    {
        replaceTopFragment(errorFragment);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void replaceTopFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(android.R.id.content, fragment).commit();
        fragmentManager.executePendingTransactions();
    }

    private void removeTopFragment()
    {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Fragment topFragment = fragmentManager.findFragmentById(android.R.id.content);
        if (null != topFragment)
        {
            fragmentManager.beginTransaction()
                    .remove(topFragment)
                    .commit();
            fragmentManager.executePendingTransactions();
        }
    }

    class JokesAdapter extends RecyclerView.Adapter<JokesAdapter.ViewHolder>
    {
        private List<Joke> mJokes;

        public JokesAdapter(List<Joke> jokes)
        {
            mJokes = jokes;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View jokeView = inflater.inflate(R.layout.item_joke, parent, false);

            return new ViewHolder(jokeView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position)
        {
            Joke joke = mJokes.get(position);
            holder.jokeTextView.setText(joke.joke);
            Glide
                    .with(JokesFragment.this)
                    .load(joke.gifUrl)
                    .asBitmap()
                    .into(holder.jokeImageView);
        }

        @Override
        public int getItemCount()
        {
            return mJokes.size();
        }

        public void refreshData(List<Joke> jokes)
        {
            mJokes = jokes;
            notifyDataSetChanged();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
        {
            private final TextView jokeTextView;
            private final ImageView jokeImageView;

            public ViewHolder(View itemView)
            {
                super(itemView);
                jokeTextView = (TextView) itemView.findViewById(R.id.joke_text);
                jokeImageView = (ImageView) itemView.findViewById(R.id.joke_image);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view)
            {
                mUserActionsListener.openJokeDetail(mJokes.get(getAdapterPosition()));
            }
        }
    }
}
