package it.dindonkey.chucknorrisjokes.jokes;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import it.dindonkey.chucknorrisjokes.App;
import it.dindonkey.chucknorrisjokes.R;
import it.dindonkey.chucknorrisjokes.databinding.FragmentErrorBinding;
import it.dindonkey.chucknorrisjokes.events.ReloadJokesEvent;
import it.dindonkey.chucknorrisjokes.events.RxBus;

public class ErrorFragment extends Fragment
{
    @Inject
    RxBus mRxBus;

    public static ErrorFragment newInstance()
    {
        return new ErrorFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ((App) getActivity().getApplication()).getComponent().inject(this);
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
        mRxBus.post(new ReloadJokesEvent());
    }

}
