package com.cdhi.services;

import com.cdhi.domain.Board;
import com.cdhi.domain.Card;
import com.cdhi.domain.User;
import com.cdhi.domain.enums.Column;
import com.cdhi.repositories.BoardRepository;
import com.cdhi.repositories.CardRepository;
import com.cdhi.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;

@Service
@Slf4j
public class DBService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    BCryptPasswordEncoder CRYPTER;

    @Transactional
    public void instantiateTestDatabase() throws ParseException {

        User user1 = new User("Jorge", "jorgesilva@gmail.com", CRYPTER.encode("123456"));
        User user2 = new User("Caio", "caiosilveiranunes@piririm.com", CRYPTER.encode("123123"));
        User user3 = new User("Paulinho", "paulinho@pau.linho", CRYPTER.encode("456456"));
        user1.setEnabled(true);
        user2.setEnabled(true);
        user3.setEnabled(true);
        user1.set_key(null);
        user2.set_key(null);
        user3.set_key(null);

        Board board1 = new Board("Board 1", user1);
        Board board2 = new Board("Board 2", user1);
        Board board3 = new Board("Board 3", user2);
        Board board4 = new Board("Board 4", user3, "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.");

        Card card1 = new Card(Column.BACKLOG, 0, "Card 1", "jaz aqui o card 1", Calendar.getInstance().getTime(), Calendar.getInstance().getTime());
        Card card2 = new Card(Column.TODO, 0, "Card 2", "este é o card 2", Calendar.getInstance().getTime(), Calendar.getInstance().getTime());

        board4.getCards().addAll(Arrays.asList(card1, card2));
        card1.setBoard(board4);
        card2.setBoard(board4);

        card1.getUsers().add(user1);
        user1.getCards().add(card1);

        card2.getUsers().addAll(Arrays.asList(user2, user3));
        user2.getCards().add(card2);
        user3.getCards().add(card2);

        user1.getMyBoards().add(board1);
        user1.getBoards().add(board1);
        board1.getUsers().add(user1);
        board1.setOwner(user1);

        user1.getMyBoards().add(board2);
        user1.getBoards().addAll(Arrays.asList(board2, board4));
        board2.getUsers().add(user1);
        board2.setOwner(user1);

        user2.getMyBoards().add(board3);
        user2.getBoards().addAll(Arrays.asList(board3, board4));
        board3.getUsers().add(user2);
        board3.setOwner(user2);

        user2.getBoards().add(board2);
        board2.getUsers().add(user2);

        user3.getMyBoards().add(board4);
        user3.getBoards().addAll(Arrays.asList(board1, board2, board3, board4));
        board1.getUsers().add(user3);
        board2.getUsers().add(user3);
        board3.getUsers().add(user3);
        board4.getUsers().addAll(Arrays.asList(user1, user2, user3));
        board4.setOwner(user3);

        userRepository.saveAll(Arrays.asList(user1, user2, user3));
        boardRepository.saveAll(Arrays.asList(board1, board2, board3, board4));
        cardRepository.saveAll(Arrays.asList(card1, card2));

        log.info("Cards do User {}: {}", user2, user2.getCards());
    }
}
