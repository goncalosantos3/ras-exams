import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

public class teste {
    public static void main(String[] args){
        String teste = "33333333-3333-3333-3333-333333333333";
        UUID id1 = UUID.nameUUIDFromBytes(teste.getBytes());
        UUID id2 = UUID.nameUUIDFromBytes(teste.getBytes());

        if(id1.equals(id2)){
            System.out.println("São iguais!");
        }else{
            System.out.println("Não são iguais!");
        }

        String teste1 = id1.toString();
        String teste2 = id2.toString();

        if(teste1.equals(teste2)){
            System.out.println("São iguais!");
        }else{
            System.out.println("Não são iguais!");
        }
        System.out.println(teste1);
        System.out.println(teste2);

        Map<UUID, Integer> map = new HashMap<>();
        map.put(id1, 1020);
        System.out.println(map.get(id2));
    }
}
