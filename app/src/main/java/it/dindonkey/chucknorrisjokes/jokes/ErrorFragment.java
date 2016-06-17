package it.dindonkey.chucknorrisjokes.jokes;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.dindonkey.chucknorrisjokes.App;
import it.dindonkey.chucknorrisjokes.R;
import it.dindonkey.chucknorrisjokes.databinding.FragmentErrorBinding;
import it.dindonkey.chucknorrisjokes.events.ReloadJokesEvent;

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
        FragmentErrorBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_error,
                container,
                false);
        binding.setHandlers(this);
        return binding.getRoot();
    }

    public void onRetryButtonClick(View view)
    {
        ((App) getActivity().getApplication()).getRxBus().post(new ReloadJokesEvent());
    }

}
