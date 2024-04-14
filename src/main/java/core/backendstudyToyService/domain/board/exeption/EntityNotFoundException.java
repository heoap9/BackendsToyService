package core.backendstudyToyService.domain.board.exeption;



//JPA범위내에서 데이터가 없다면 에러 뿜기
public class EntityNotFoundException extends RuntimeException{
    public EntityNotFoundException(String message){
        super(message);
    }
}
