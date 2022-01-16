public class Animal {
    final String name;
    public Animal(String name) {
    this.name = name;
    }
    public String bark() {
    return this.name;
    }
    public static void main(String[] args) {
        Dog dog = new Dog();
    dog.bark(); 
    Animal animal = new Dog();
    animal.bark(); // No such method
    }
}


    class Dog extends Animal {
    public Dog() {
    super("dog");
    }
    public String bark() {
    return "Woof!";
    }
    }
    