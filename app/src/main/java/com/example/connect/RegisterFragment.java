package com.example.connect;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterFragment extends Fragment {

    EditText id, name, password, passwordcheck;
    Button regiBtn;
    ImageView profile;
    ImageView profileBtn;

    Context context;

    private final int GET_GALLERY_IMAGE = 200;

    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}; //권한 설정 변수
    private static final int MULTIPLE_PERMISSIONS = 101; //권한 동의 여부 문의 후 CallBack 함수에 쓰일 변수

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_register, container, false);

        context = container.getContext();
        //EditText v.findViewById
        id = v.findViewById(R.id.reg_id);
        //nickname = v.findViewById(R.id.reg_nickname);
        name = v.findViewById(R.id.reg_name);
        password = v.findViewById(R.id.reg_password);
        password.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        passwordcheck = v.findViewById(R.id.reg_passwordcheck);
        passwordcheck.setTransformationMethod(new AsteriskPasswordTransformationMethod());

        profile = v.findViewById(R.id.profile_register);
        profileBtn = v.findViewById(R.id.profileBtn);
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissions();
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();// FireBase 현재 Auth 정보 가져오기

        regiBtn = v.findViewById(R.id.btn_register); //Button v.findViewById
        regiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //회원가입 버튼이 클릭되었을 때

                if (!id.getText().toString().equals("") && !name.getText().toString().equals("") &&
                        !password.getText().toString().equals("")
                        && !passwordcheck.getText().toString().equals("")
                        && !profile.getDrawable().equals(getResources().getDrawable(R.drawable.app_icon))) { //모든 항목이 기입되었을 경우

                    Bitmap img = ((BitmapDrawable) profile.getDrawable()).getBitmap();
                    ByteArrayOutputStream outStream = new ByteArrayOutputStream();

                    img.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                    byte[] imageOut = outStream.toByteArray();
                    String profileImageBase64 = Base64.encodeToString(imageOut, Base64.NO_WRAP);

                    createAccount(id.getText().toString(), password.getText().toString(), passwordcheck.getText().toString(), profileImageBase64); //기입한 정보로 회원가입 진행
                }
                else { //채워지지 않은 항목이 있을 경우
                    Toast.makeText(getContext(), "빈칸을 채워주세요", Toast.LENGTH_SHORT).show(); //빈칸 기입 요청 토스트
                }
            }
        });

        return v;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == GET_GALLERY_IMAGE && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {

            Uri selectedImageUri = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

//            Matrix rotateMatrix = new Matrix();
//            rotateMatrix.postRotate(90); //-360~360
//
//            bitmap =  Bitmap.createBitmap(bitmap, 0, 0,
//                    bitmap.getWidth(), bitmap.getHeight(), rotateMatrix, false);

            Bitmap thumbImage = ThumbnailUtils.extractThumbnail(bitmap, 480, 480);
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            thumbImage.compress(Bitmap.CompressFormat.JPEG, 10, bs); //이미지가 클 경우 OutOfMemoryException 발생이 예상되어 압축

            profile.setImageBitmap(thumbImage);

        }

    }

    private boolean isValidPasswd(String target) { //패스워드 유효성 검사 함수
        Pattern p = Pattern.compile("(^.*(?=.{4,50})(?=.*[0-9])(?=.*[a-z]).*$)"); //패스워드 검사 정규식. 4~50자, 알파벳+숫자
        Matcher m = p.matcher(target); //정규식 대입 검사
        return m.find() && !target.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*"); //정규식 검사값이 옳고 한글이 포함되지 않았다면 true 반환
    }

    private boolean isValidEmail(String target) { //Email 유효성 검사 함수

        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches(); //Email 유효성 검사 결과 반환
    }

    private void createAccount(final String email, final String password, final String passwordcheck, final String profile) { //회원가입 함수

        //ProgressBar v.findViewById
//        regiProgress = v.findViewById(R.id.progress_regi);
//
//        regiProgress.setVisibility(View.VISIBLE); //ProgressBar 표시

        if (!isValidEmail(email)) { //이메일 유효성 검사에 실패할 경우
            Toast.makeText(getContext(), "이메일이 유효하지 않습니다", Toast.LENGTH_SHORT).show(); //이메일 유효성검사 실패 토스트
            //regiProgress.setVisibility(View.INVISIBLE); //ProgressBar 숨기기
            return;
        }

        if (!isValidPasswd(password)) { //패스워드 유효성 검사에 실패할 경우
            Toast.makeText(getContext(), "패스워드가 유효하지 않습니다", Toast.LENGTH_SHORT).show(); //패스워드 유효성 검사 실패 토스트
            //regiProgress.setVisibility(View.INVISIBLE); //ProgressBar 숨기기
            return;
        }

        if (!password.equals(passwordcheck)) { //패스워드와 패스워드 재입력이 일치하지 않는 경우
            Toast.makeText(getContext(), "패스워드가 일치하지 않습니다", Toast.LENGTH_SHORT).show(); //재입력 불일치 토스트
            //regiProgress.setVisibility(View.INVISIBLE); //ProgressBar 숨기기
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password) //Firebase에 회원가입 요청
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() { //작업 완료 리스너
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) { //회원가입에 성공했다면
                            FirebaseUser user = firebaseAuth.getCurrentUser(); //현재 유저 정보 가져오기
                            UserModel userModel = new UserModel(name.getText().toString(), email, user.getUid(), profile); //UserModel 양식에 회원가입 정보 추가
                            databaseReference.child("user").push().setValue(userModel); //작성한 model 양식을 Firebase DB에 등록

                            CardModel cardModel = new CardModel(name.getText().toString(), "이메일이 없습니다", user.getUid(), profile,
                                    "전화번호가 없습니다", "직장 정보가 없습니다", "직업 정보가 없습니다");
                            databaseReference.child("card").push().setValue(cardModel);
                            //Toast.makeText(getContext(), "환영합니다, " + id.getText().toString() + "!", Toast.LENGTH_SHORT).show(); //회원가입 성공 토스트
                            startActivity(new Intent(getContext(), DBLoadActivity.class));
                            getActivity().finish();
                        }
                        else { //회원가입에 실패했다면
                            Toast.makeText(getContext(), "회원가입 실패", Toast.LENGTH_SHORT).show(); //화원가입 실패 토스트
                            //regiProgress.setVisibility(View.INVISIBLE); //ProgressBar 숨기기
                        }
                    }
                });


    }

    public static class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new AsteriskPasswordTransformationMethod.PasswordCharSequence(source);
        }

        static class PasswordCharSequence implements CharSequence {
            private CharSequence mSource;
            public PasswordCharSequence(CharSequence source) {
                mSource = source;
            }
            public char charAt(int index) {
                return '*';
            }
            public int length() {
                return mSource.length();
            }
            public CharSequence subSequence(int start, int end) {
                return mSource.subSequence(start, end);
            }
        }
    }

    private boolean checkPermissions() {
        int result;
        List<String> permissionList = new ArrayList<>();
        for (String pm : permissions) {
            result = ContextCompat.checkSelfPermission(context, pm);
            if (result != PackageManager.PERMISSION_GRANTED) { //사용자가 해당 권한을 가지고 있지 않을 경우 리스트에 해당 권한명 추가
                permissionList.add(pm);
            }
        }
        if (!permissionList.isEmpty()) { //권한이 추가되었으면 해당 리스트가 empty가 아니므로 request 즉 권한을 요청합니다.
            ActivityCompat.requestPermissions(getActivity(), permissionList.toArray(new String[permissionList.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++) {
                        if (permissions[i].equals(this.permissions[0])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();
                            }
                        } else if (permissions[i].equals(this.permissions[1])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();

                            }
                        } else if (permissions[i].equals(this.permissions[2])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();

                            }
                        }
                    }
                } else {
                    showNoPermissionToastAndFinish();
                }
                return;
            }
        }
    }


    //권한 획득에 동의를 하지 않았을 경우 아래 Toast 메세지를 띄우며 해당 Activity를 종료시킵니다.
    private void showNoPermissionToastAndFinish() {
        Toast.makeText(context, "권한이 부족해 종료합니다.", Toast.LENGTH_SHORT).show();
        getActivity().finish();
    }
}
