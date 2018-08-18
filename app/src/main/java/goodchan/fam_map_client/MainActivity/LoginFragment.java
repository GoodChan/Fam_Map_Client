package goodchan.fam_map_client.MainActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import goodchan.fam_map_client.Model.Data;
import goodchan.fam_map_client.Proxy;
import goodchan.fam_map_client.R;
import goodchan.fam_map_client.Requests.LoginRequest;
import goodchan.fam_map_client.Requests.RegisterRequest;
import goodchan.fam_map_client.Responses.*;
import java.util.ArrayList;
import java.util.logging.*;

public class LoginFragment extends Fragment {
    private static Logger logger;

    static {
        logger = Logger.getLogger("Client");
    }

    protected final String LOGIN_STRING = "login";
    protected final String REGISTER_STRING = "register";
    Proxy proxy = null;
    Button loginButton;
    Button registerButton;
    EditText editServerHost;
    EditText editServerPort;
    EditText editUserName;
    EditText editPassword;
    EditText editFirstName;
    EditText editLastName;
    EditText editEmail;
    ArrayList<EditText> editTexts = new ArrayList<EditText>();
    String gender = ""; //temporary gender holder for logging in with proxy


    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        ArrayList<EditText> editTexts = new ArrayList<EditText>();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private RegisterRequest extractFromUI(View v) {
        logger.entering("MainButtonThread", "extractFromUI");
        logger.info("in RegisterRequest");
        String serverHost = convertToString(editServerHost);
        String serverPort = convertToString(editServerPort);
        String userName = convertToString(editUserName);
        String password = convertToString(editPassword);
        String firstName = convertToString(editFirstName);
        String lastName = convertToString(editLastName);
        String email = convertToString(editEmail);
        proxy = new Proxy(serverHost, serverPort);
        return new RegisterRequest(userName, password, email, firstName, lastName, gender);
    }
    private String convertToString(EditText editText) {
        logger.info("in convert ot string");
        return editText.getText().toString();
    }

    private class MainButtonThread extends AsyncTask<RegisterRequest, String, String> { //last param is execute type and doinbackground return type
        View v = null;
        String genderMainButton = "";
        String task = ""; //decides logging in or registering see finals above

        public MainButtonThread(View v, String task, String g) {
            this.v = v;
            this.task = task;
            this.genderMainButton = g;
        }

        @Override
        protected String doInBackground(RegisterRequest... request) { //TODO, does this actually run on a new thread?
            logger.entering("MainButtonThread", "doInBackground");
            return login(request[0]);
        }

        public String login(RegisterRequest request) { //most of this class checks for message responses/errors coming back to display in a toast
            Response userEnterAppResponse = null;
            logger.entering("MainButtonThread", "login Gender = " + genderMainButton);

            try {
                if (task.equals(LOGIN_STRING)) {
                    userEnterAppResponse = proxy.logIn(new LoginRequest(request.getUserName(), request.getPassword()));
                } else if (task.equals(REGISTER_STRING)) {
                    userEnterAppResponse = proxy.register(new RegisterRequest(request.getUserName(), request.getPassword(), request.getEmail(),
                            request.getFirstName(), request.getLastName(), genderMainButton));
                }

                //checks for errors with login request
                if (userEnterAppResponse == null) {
                    loginButton.setEnabled(true);
                    return "Invalid request";
                } else if (userEnterAppResponse.getClass() == MessageResponse.class) {
                    MessageResponse messageResponse = (MessageResponse) userEnterAppResponse;
                    loginButton.setEnabled(true);
                    return messageResponse.getMessage();

                //fetches events and people for this user if the response is correct
                } else if (userEnterAppResponse.getClass() == UserResponse.class) {
                    Response getEventsResponse = proxy.getEvents((UserResponse) userEnterAppResponse);
                    Response getPeopleResponse = proxy.getPeople((UserResponse) userEnterAppResponse);

                    //checks for failures in fetching the data
                    if (getEventsResponse.getClass() == MessageResponse.class) {
                        MessageResponse messageResponse = (MessageResponse) userEnterAppResponse;
                        loginButton.setEnabled(true);
                        return messageResponse.getMessage();
                    } else if (getPeopleResponse.getClass() == MessageResponse.class) {
                        MessageResponse messageResponse = (MessageResponse) userEnterAppResponse;
                        loginButton.setEnabled(true);
                        return messageResponse.getMessage();
                    } else {

                        //no errors from server, extract info into RAM
                        Data.extractUserDataToRAM((UserResponse) userEnterAppResponse,
                                ((EventDataResponse) getEventsResponse).getData(),
                                ((PersonDataResponse) getPeopleResponse).getData());

                        String userPersonID = ((UserResponse) userEnterAppResponse).getPersonID();

                        return ("Welcome " + Data.people.get(userPersonID).getFirstName() +
                                " " + Data.people.get(userPersonID).getLastName());
                    }
                }
            } catch (Exception e) {
                loginButton.setEnabled(true);
                return e.getMessage();
            }
            return "Error: login not properly filtered";
        }

        protected void onPostExecute(String message) {
            Toast.makeText(v.getContext(), message, Toast.LENGTH_SHORT).show();
            if (message.contains("Welcome")) {
                Fragment mapFragment = new MainMapFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.mainFrameLayout, mapFragment);
                fragmentTransaction.commit();
            }

        }

    }

    private void checkFields() {
        boolean canRegister = true;
        boolean canLogin = true;
        logger.info("check fields entered");

        for (EditText et : editTexts) {
            String s = et.getText().toString();
            if (s.isEmpty()) {
                canRegister = false;
            }
        }
        if (gender.equals("f") || gender.equals("m")) {
        }
        else {
            canRegister = false;
        }

        if (editPassword.getText().toString().isEmpty()) {
            canLogin = false;
        } else if (editUserName.getText().toString().isEmpty()) {
            canLogin = false;
        }

        logger.info("can register: " + canRegister + " can login: " + canLogin);
        registerButton.setEnabled(canRegister);
        loginButton.setEnabled(canLogin);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        logger.entering("LoginFragment", "onCreateView");
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        editServerHost = v.findViewById(R.id.ServerHost);
        editServerPort = v.findViewById(R.id.ServerPort);
        editUserName = v.findViewById(R.id.UserName);
        editPassword = v.findViewById(R.id.Password);
        editFirstName = v.findViewById(R.id.FirstName);
        editLastName = v.findViewById(R.id.LastName);
        editEmail = v.findViewById(R.id.Email);

        loginButton = v.findViewById(R.id.login_button);
        registerButton = v.findViewById(R.id.register_button);
        loginButton.setEnabled(false);
        registerButton.setEnabled(false);

        editTexts.add(editServerHost);
        editTexts.add(editServerPort);
        editTexts.add(editUserName);
        editTexts.add(editPassword);
        editTexts.add(editFirstName);
        editTexts.add(editLastName);
        editTexts.add(editEmail);

        checkFields();

        editServerHost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkFields();
            }
        });

        editServerPort.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkFields();
            }
        });

        editUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkFields();
            }
        });

        editPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkFields();
            }
        });

        editFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkFields();
            }
        });


        editLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkFields();
            }
        });

        editEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkFields();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logger.info("before login button extraction");
                loginButton.setEnabled(false);
                RegisterRequest request = extractFromUI(v);
                logger.info("after login button extraction");
                new MainButtonThread(v, LOGIN_STRING, gender).execute(request);
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logger.info("before register button extraction");
                loginButton.setEnabled(false);
                RegisterRequest request = extractFromUI(v);
                logger.info("after register button extraction");
                new MainButtonThread(v, REGISTER_STRING, gender).execute(request);
            }
        });

        RadioGroup genderRadio = v.findViewById(R.id.GenderRadioGroup);
        genderRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedID) {
                logger.entering("LoginFragment", "genderButtonClicked");

                switch (checkedID) {
                    case R.id.RadioFemale:
                        gender = "f";
                        logger.info("radio = female? " + gender);
                        break;
                    case R.id.RadioMale:
                        gender = "m";
                        logger.info("radio = male? " + gender);
                        break;
                    }
                checkFields();
            }
        });

        return v;
    }
}
