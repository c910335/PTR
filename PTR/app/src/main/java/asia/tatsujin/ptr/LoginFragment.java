package asia.tatsujin.ptr;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * A placeholder fragment containing a simple view.
 */
public class LoginFragment extends Fragment {
    String mUsername,mPassword;

    View v;
    Button getInfo;

    public LoginFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_login, container, false);
        getInfo=(Button) v.findViewById(R.id.login);

        getInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserInfo();
            }
        });
        return v;
    }

    public void getUserInfo(){
        EditText user= (EditText) getView().findViewById(R.id.username);
        EditText password=(EditText) getView().findViewById(R.id.password);

        mUsername=user.getText().toString();
        mPassword=password.getText().toString();

        Log.v("login test",mUsername);
        Log.v("login test",mPassword);
    }
}
