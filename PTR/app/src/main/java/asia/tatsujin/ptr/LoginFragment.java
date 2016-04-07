package asia.tatsujin.ptr;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import asia.tatsujin.ptr.graptt.GrapttClient;
import asia.tatsujin.ptr.models.Board;

/**
 * A placeholder fragment containing a simple view.
 */
public class LoginFragment extends Fragment {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private android.support.v4.app.FragmentManager fm;
    private android.support.v4.app.FragmentTransaction ft;

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
    public void login(){
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();


        MainActivity.grapttClient.login(username, password, new GrapttClient.OnLoginListener() {
            @Override
            public void onLogin(String status) {
                Log.v("login: ", status);
                if (status.contentEquals("Other Online")) {
                    Log.v("login: ", "deleteing");
                    deleteOther(true); // other onlie delete
                } else {
                    getFavorite();

                }


            }

            @Override
            public void onError(String message) {
                Toast.makeText(getActivity(), "Error_login: " + message, Toast.LENGTH_SHORT).show();
            }
        });

        // Todo: Login
    }
    public void deleteOther(boolean flag){
        MainActivity.grapttClient.deleteOther(flag, new GrapttClient.OnDeleteOtherListener() {
            @Override
            public void onDelete(String status) {
                Log.d("del:", status.toString());
                getFavorite();
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    public void  getFavorite(){
        MainActivity.grapttClient.getFavorite(new GrapttClient.OnGetFavoriteListener() {
            @Override
            public void onGet(Board[] boards) {
                change_fragment(boards);
                Log.d("get_Favorite", boards[0].en_name);
            }

            @Override
            public void onError(String message) {
                Log.d("Fail_Favorite :", message);
            }
        });
    }

    public void change_fragment(Board[] boards){

       android.support.v4.app.FragmentTransaction fragmentTransaction =MainActivity.fragmentManager.beginTransaction();
        FavroiteFragment ff =new FavroiteFragment(boards);
        fragmentTransaction.replace(R.id.fragment,ff)
                .addToBackStack(null)
                .commit();
    }


}
