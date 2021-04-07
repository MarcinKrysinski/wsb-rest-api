package pl.krysinski.wsbrestapi;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class CarsRepository {

    Long count = 3L;

    List<Car> cars = new ArrayList<>(Arrays.asList(
            new Car(1L, "Opel", "Vectra", "Black", 2007),
            new Car(2L, "Ford", "T", "Black", 1910),
            new Car(3L, "VW", "Passat", "Black", 2020)
    ));

    public List<Car> findAll() {
        return cars;
    }

    public Car findById(Long id) {
        return cars.stream().filter(car -> car.getId().equals(id)).findFirst().orElse(null);
    }

    public Car add(Car newCar) {
        newCar.setId(++count);
        cars.add(newCar);
        return newCar;
    }

    public Car update(Long id, Car car) {
        Car carToUpdate = findById(id);

        if (carToUpdate == null) {
            return null;
        }

        carToUpdate.setMark(car.getMark());
        carToUpdate.setModel(car.getModel());
        carToUpdate.setColor(car.getColor());
        carToUpdate.setProductionYear(car.getProductionYear());

        return carToUpdate;
    }

    public void delete(Long id) {
        Car car = findById(id);
        cars.remove(car);
    }
}
