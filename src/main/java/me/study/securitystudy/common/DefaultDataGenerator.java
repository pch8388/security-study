package me.study.securitystudy.common;


import lombok.RequiredArgsConstructor;
import me.study.securitystudy.account.Account;
import me.study.securitystudy.account.AccountService;
import me.study.securitystudy.book.Book;
import me.study.securitystudy.book.BookRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultDataGenerator implements ApplicationRunner {

    private final AccountService accountService;
    private final BookRepository bookRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        final Account sc = createUser("sc");
        final Account sc2 = createUser("sc2");

        createBook("spring", sc);
        createBook("hibernate", sc2);
    }

    private void createBook(String title, Account account) {
        final Book book = new Book();
        book.setTitle(title);
        book.setAuthor(account);
        bookRepository.save(book);
    }

    private Account createUser(String username) {
        Account account = new Account();
        account.setUsername(username);
        account.setPassword("123");
        account.setRole("USER");
        return accountService.createNew(account);
    }
}
