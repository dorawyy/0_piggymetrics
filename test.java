public interface Item {...}

public class ItemImpl implements Item {...} 

public class Store {
    private Item item; 
    
    public Store(){
        this.item = new ItemImpl(); // construct the object, not DI 
    }

    public Store (Item item) { // provide the constructed object, DI
        this.item = item; 
    }
}
