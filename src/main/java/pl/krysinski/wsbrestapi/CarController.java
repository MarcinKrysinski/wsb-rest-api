package pl.krysinski.wsbrestapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("cars")
public class CarController {

    final CarsRepository carsRepository;

    @Autowired
    public CarController(CarsRepository carsRepository) {
        this.carsRepository = carsRepository;
    }

    @GetMapping
    CollectionModel<EntityModel<Car>> findAll() {
        List<EntityModel<Car>> cars = carsRepository.findAll().stream()
                .map(car -> EntityModel.of(car, linkTo(methodOn(CarController.class).findById(car.getId())).withSelfRel()))
                .collect(Collectors.toList());
        return CollectionModel.of(cars, linkTo(methodOn(CarController.class).findAll()).withSelfRel());
    }

    @GetMapping("{id}")
    ResponseEntity<?> findById(@PathVariable Long id) {
        Car car = carsRepository.findById(id);
        if (car != null) {
            EntityModel<Car> carModel = EntityModel.of(car,
                    linkTo(methodOn(CarController.class).findById(id)).withSelfRel(),
                    linkTo(methodOn(CarController.class).findAll()).withRel("cars"));
            return ResponseEntity.ok().body(carModel);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    ResponseEntity<?> create(@RequestBody Car car) {
        Car savedCar = carsRepository.add(car);
        if (savedCar != null) {
            EntityModel<Car> carModel = EntityModel.of(car,
                    linkTo(methodOn(CarController.class).findById(savedCar.getId())).withSelfRel());
            return ResponseEntity.created(carModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(carModel);
        } else {
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @PutMapping("{id}")
    ResponseEntity<?> update(@PathVariable Long id, @RequestBody Car car) {
        Car updatedCar = carsRepository.update(id, car);

        if (updatedCar == null) {
            return ResponseEntity.notFound().build();
        } else {
            EntityModel<Car> updatedCarModel = EntityModel.of(updatedCar,
                    linkTo(methodOn(CarController.class).findById(id)).withSelfRel());
            return ResponseEntity.ok().body(updatedCarModel);
        }
    }

    @DeleteMapping("{id}")
    ResponseEntity<?> delete(@PathVariable Long id) {
        carsRepository.delete(id);
        return ResponseEntity.noContent().build();
    }


}
