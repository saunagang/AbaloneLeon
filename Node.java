public class Node implements Cloneable {

    playingField field;
    Node[] children;
    Node parent;
    int limit;

    public Node(playingField field){
        this.field = field;
        this.children = new Node[6];
        this.parent = null;
        initChildren();
    }

    public void initChildren(){

        for (int i = 0; i < 6; i++){
            children[i] = new Node(field.clonen());
            children[i].parent = this;

        }
    }








    public static void main(String[] args) {

        playingField field = new playingField(9,Player.HUMAN);

        Node node = new Node(field);

        node.initChildren();
    }


}