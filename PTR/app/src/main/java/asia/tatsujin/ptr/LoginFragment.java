package asia.tatsujin.ptr;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import asia.tatsujin.ptr.graptt.GrapttClient;

public class LoginFragment extends Fragment {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private OnTransactListener onTransactListener;

    private View view;
    public LoginFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);

        loginButton = (Button) view.findViewById(R.id.login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameEditText = (EditText) view.findViewById(R.id.username);
                passwordEditText = (EditText) view.findViewById(R.id.password);
                login();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onTransactListener = (OnTransactListener) getActivity();
    }

    public void login(){
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        onTransactListener.onStartTransaction();
        MainActivity.grapttClient.login(username, password, new GrapttClient.OnLoginListener() {
            @Override
            public void onLogin(String status) {
                Log.w("GrapttClient", status);
                if (status.contentEquals("Other Online")) {
                    Log.w("GrapttClient", "deleteing other");
                    deleteOther(true); // other onlie delete
                } else
                    showFavorite();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getActivity(), "Error: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteOther(boolean delete){
        MainActivity.grapttClient.deleteOther(delete, new GrapttClient.OnDeleteOtherListener() {
            @Override
            public void onDelete(String status) {
                Log.w("GrapttClient", status);
                showFavorite();
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    public void showFavorite(){
        ((ViewGroup) getView()).removeAllViews();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        FavoriteFragment favoriteFragment = new FavoriteFragment();
        fragmentTransaction.replace(R.id.fragment, favoriteFragment)
                .commit();
    }


}
