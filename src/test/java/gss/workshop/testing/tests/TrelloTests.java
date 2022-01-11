package gss.workshop.testing.tests;

import gss.workshop.testing.pojo.board.BoardCreationRes;
import gss.workshop.testing.pojo.card.CardCreationRes;
import gss.workshop.testing.pojo.list.ListCreationRes;
import gss.workshop.testing.requests.RequestFactory;
import gss.workshop.testing.utils.ConvertUtils;
import gss.workshop.testing.utils.OtherUtils;
import gss.workshop.testing.utils.ValidationUtils;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class TrelloTests extends TestBase {

  @Test
  public void trelloWorkflowTest() {
    // 1. Create new board without default list
    String boardName = OtherUtils.randomName(OBJ_BOARD);
    Response resBoardCreation = RequestFactory.createBoard(boardName, false);

    // VP. Validate status code
    ValidationUtils.validateStatusCode(resBoardCreation, STATUS_CODE_200);

    // VP. Validate a board is created: Board name and permission level
    BoardCreationRes board =
        ConvertUtils.convertRestResponseToPojo(resBoardCreation, BoardCreationRes.class);
    ValidationUtils.validateStringEqual(boardName, board.getName());
    ValidationUtils.validateStringEqual("private", board.getPrefs().getPermissionLevel());

    // -> Store board Id
    String boardId = board.getId();
    System.out.println(String.format("Board Id of the new Board: %s", boardId));

    // 2. Create a TODO list
    Response resToDoListCreation = RequestFactory.createList(boardId, LIST_TODO);

    // VP. Validate status code
    ValidationUtils.validateStatusCode(resToDoListCreation, STATUS_CODE_200);

    // VP. Validate a list is created: name of list, closed attribute
    ListCreationRes listToDo =
            ConvertUtils.convertRestResponseToPojo(resToDoListCreation, ListCreationRes.class);
    ValidationUtils.validateStringEqual(LIST_TODO, listToDo.getName());
    ValidationUtils.validateFalseValue(listToDo.getClosed());

    // VP. Validate the list was created inside the board: board Id
    ValidationUtils.validateStringEqual(boardId, listToDo.getIdBoard());

    // -> Store ToDo list Id
    String listToDoId = listToDo.getId();
    System.out.println(String.format("ToDo list Id: %s", listToDoId));

    // 3. Create a DONE list
    Response resDoneListCreation = RequestFactory.createList(boardId, LIST_DONE);

    // VP. Validate status code
    ValidationUtils.validateStatusCode(resDoneListCreation, STATUS_CODE_200);

    // VP. Validate a list is created: name of list, closed attribute
    ListCreationRes listDone =
            ConvertUtils.convertRestResponseToPojo(resDoneListCreation, ListCreationRes.class);
    ValidationUtils.validateStringEqual(LIST_DONE, listDone.getName());
    ValidationUtils.validateFalseValue(listDone.getClosed());

    // VP. Validate the list was created inside the board: board Id
    ValidationUtils.validateStringEqual(boardId, listDone.getIdBoard());

    // -> Store Done list Id
    String listDoneId = listDone.getId();
    System.out.println(String.format("Done list Id: %s", listDoneId));

    // 4. Create a new Card in TODO list
    String cardName = OtherUtils.randomName(OBJ_CARD);
    Response resCardCreation = RequestFactory.createCard(cardName, listToDoId);

    // VP. Validate status code
    ValidationUtils.validateStatusCode(resCardCreation, STATUS_CODE_200);

    // VP. Validate a card is created: task name, list id, board id
    CardCreationRes card =
            ConvertUtils.convertRestResponseToPojo(resCardCreation, CardCreationRes.class);
    ValidationUtils.validateStringEqual(cardName, card.getName());
    ValidationUtils.validateStringEqual(listToDoId, card.getIdList());
    ValidationUtils.validateStringEqual(boardId, card.getIdBoard());

    // VP. Validate the card should have no votes or attachments
    ValidationUtils.validateStringEqual(ZERO, card.getBadges().getVotes().toString());
    ValidationUtils.validateEmptyList(card.getAttachments());

    // 5. Move the card to DONE list
    Response resMovingCard = RequestFactory.updateCard(card.getId(), listDoneId);

    // VP. Validate status code
    ValidationUtils.validateStatusCode(resMovingCard, STATUS_CODE_200);

    // VP. Validate the card should have new list: list id
    CardCreationRes updatedCard =
            ConvertUtils.convertRestResponseToPojo(resMovingCard, CardCreationRes.class);
    ValidationUtils.validateStringEqual(listDoneId, updatedCard.getIdList());

    // VP. Validate the card should preserve properties: name task, board Id, "closed" property
    ValidationUtils.validateStringEqual(cardName, updatedCard.getName());
    ValidationUtils.validateStringEqual(boardId, updatedCard.getIdBoard());
    ValidationUtils.validateFalseValue(updatedCard.getClosed());

    // 6. Get board before deleting
    Response resGettingBoardBeforeDeleting = RequestFactory.getBoardById(boardId);
    // VP. Validate status code
    ValidationUtils.validateStatusCode(resGettingBoardBeforeDeleting, STATUS_CODE_200);
    // VP. Validate a board is listed: Board name
    BoardCreationRes resBoard =
            ConvertUtils.convertRestResponseToPojo(resGettingBoardBeforeDeleting, BoardCreationRes.class);
    ValidationUtils.validateStringEqual(boardName, resBoard.getName());

    // 7. Delete board
    Response resBoardDeletion = RequestFactory.deleteBoard(boardId);
    // VP. Validate status code
    ValidationUtils.validateStatusCode(resBoardDeletion, STATUS_CODE_200);

    // 8. Get board after deleting
    Response resGettingBoardAfterDeleting = RequestFactory.getBoardById(boardId);
    // VP. Validate status code
    ValidationUtils.validateStatusCode(resGettingBoardAfterDeleting, STATUS_CODE_404);

  }
}
