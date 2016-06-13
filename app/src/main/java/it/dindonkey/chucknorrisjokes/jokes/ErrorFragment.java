package it.dindonkey.chucknorrisjokes.jokes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.dindonkey.chucknorrisjokes.R;

public class ErrorFragment extends Fragment
{
    public static final String TAG = "error_fragment";

    public static ErrorFragment newInstance()
    {
        return new ErrorFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_error, container, false);
    }

}
