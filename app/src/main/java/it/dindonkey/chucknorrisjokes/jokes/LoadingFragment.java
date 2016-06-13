package it.dindonkey.chucknorrisjokes.jokes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.dindonkey.chucknorrisjokes.R;

public class LoadingFragment extends Fragment
{
    public static final String TAG = "loading_fragment";

    public static LoadingFragment newInstance()
    {
        return new LoadingFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_loading, container, false);
    }
}
