package ngoquangdai.myjob.service;

import ngoquangdai.myjob.entity.Account;
import ngoquangdai.myjob.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;

/*
* Có nhiệm vụ tìm user theo username
* và trả về đối tượng userDetails*/
@Service
@Transactional
public class AccountService implements UserDetailsService{
    @Autowired
    AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
//        Tìm account theo user name trong bang account
        Account account = accountRepository.findAccountByUsername(username);
//      Tạo danh sách quyền. ( một người dùng có thể có nhiều quyền).
//        Có thể tạo ra quyền riêng mapping n - n với account
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if (account.getRole() == 1){
//            thêm quyền thoe trường role trong account
            authorities.add(new SimpleGrantedAuthority("user"));
        } else if (account.getRole() == 2){
            authorities.add(new SimpleGrantedAuthority("admin"));
        } else {
            authorities.add(new SimpleGrantedAuthority("guest"));
        }
//        Tạo đối tượng user detail theo thông tin username, password và quyền được lấy ra ở trên.
//        trong đó password là pass được mã hóa
        return new User(account.getUsername(), account.getPasswordHash(), authorities);
    }
}
