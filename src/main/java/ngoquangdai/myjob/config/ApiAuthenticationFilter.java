package ngoquangdai.myjob.config;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ngoquangdai.myjob.entity.dto.CredentialDto;
import ngoquangdai.myjob.entity.dto.LoginDto;
import ngoquangdai.myjob.util.JwtUtil;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class ApiAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

//    Kiểm tra thông tin đăng nhập
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException{
        log.info("Checking login information");
        try{
//            lấy dữ liệu từ trong body cảu request
            String  jsonData = request.getReader().lines().collect(Collectors.joining());
//            Tạo đối tượng gson phục vụ cho việc parse( ép kiểu về login dto)
            Gson gson = new Gson();
            LoginDto loginDto = gson.fromJson(jsonData, LoginDto.class);
//            chuyển dữ liệu từ login dto sang
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
//            tiến hành check thỗng tin đăng nhập bằng AuthenticationManager
            return authenticationManager.authenticate(authenticationToken);
        }catch (IOException e){
            return null;
        }
    }

//    Xử lí khi đăng nhập thành công
//    trả về access token.
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException{
        User user = (User) authResult.getPrincipal();// get user that successfully login
//        generate tokens
        String accessToken = JwtUtil.generateToken(user.getUsername(),
                user.getAuthorities().iterator().next().getAuthority(),
                request.getRequestURL().toString(),
                JwtUtil.ONE_DAY * 7);
//        generate refreshToken
        String refreshToken = JwtUtil.generateToken(user.getUsername(),
                user.getAuthorities().iterator().next().getAuthority(),
                request.getRequestURL().toString(),
                JwtUtil.ONE_DAY * 14);
        CredentialDto credential = new CredentialDto(accessToken, refreshToken);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        Gson gson = new Gson();
        response.getWriter().println(gson.toJson(credential));
    }

//    Xử lí khi đăng nhập không thành công, thông báo lỗi, trả về error ở dạng json
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
            throws IOException, ServletException {
        HashMap<String, String> errors = new HashMap<>();
        errors.put("message", "Invalid information");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        Gson gson = new Gson();
        response.getWriter().println(gson.toJson(errors));
    }
}
