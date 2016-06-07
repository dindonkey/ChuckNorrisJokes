package it.dindonkey.chucknorrisjokes.jokes;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import it.dindonkey.chucknorrisjokes.App;
import it.dindonkey.chucknorrisjokes.R;
import it.dindonkey.chucknorrisjokes.data.Joke;

public class JokesFragment extends Fragment
{
    private JokesContract.UserActionsListener mJokesUserActionsListener;
    private JokesAdapter mJokesAdapter;

    public static JokesFragment newInstance()
    {
        return new JokesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mJokesAdapter = new JokesAdapter(new ArrayList<Joke>(0));
        mJokesUserActionsListener = ((App) getActivity().getApplication()).getJokesUserActionsListener();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_jokes, container, false);

        RecyclerView jokesRecyclerView = (RecyclerView) rootView.findViewById(R.id.jokes_list);
        jokesRecyclerView.setAdapter(mJokesAdapter);
        jokesRecyclerView.setHasFixedSize(true);
        jokesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        return rootView;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mJokesUserActionsListener.loadJokes();
    }

    public void refreshJokes(List<Joke> jokes)
    {
        mJokesAdapter.refreshData(jokes);
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

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            private final TextView jokeTextView;

            public ViewHolder(View itemView)
            {
                super(itemView);
                jokeTextView = (TextView) itemView.findViewById(R.id.joke_text);
            }
        }
    }
}
