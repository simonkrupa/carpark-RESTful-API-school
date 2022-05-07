package sk.stuba.fei.uim.vsa.pr2.service;

import sk.stuba.fei.uim.vsa.pr2.entities.*;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

public class CarParkService extends  AbstractCarParkService{

    public void persist(Object... entities) {
        EntityManager manager = emf.createEntityManager();

        EntityTransaction transaction = manager.getTransaction();
        transaction.begin();
        for (Object entity : entities) {
            manager.persist(entity);
        }
        transaction.commit();
        manager.close();
    }


    @Override
    public CarPark createCarPark(String name, String address, Integer pricePerHour) {
        if (name == null){
            return null;
        }
        if (pricePerHour == null){
            return null;
        }
        List<CarPark> carParks = getCarParks();
        for (Object cp: carParks) {
           if(cp instanceof CarPark){
               if(name.equals(((CarPark) cp).getName())){
                   return null;
               }
           }
        }
        CarPark carPark = new CarPark();
        carPark.setName(name);
        carPark.setAddress(address);
        carPark.setPricePerHour(pricePerHour);
        persist(carPark);
        return carPark;
    }

    @Override
    public CarPark getCarPark(Long carParkId) {
        EntityManager manager = emf.createEntityManager();
        CarPark carPark = manager.find(CarPark.class, carParkId);
        manager.close();
        System.out.println(carPark);
        return carPark;
    }

    @Override
    public CarPark getCarPark(String carParkName) {
        try {
            EntityManager manager = emf.createEntityManager();
            Query query = manager.createNamedQuery("findByName");
            query.setParameter("name", carParkName);
            return (CarPark) query.getSingleResult();
        } catch (NoResultException e){
            return null;
        }
    }

    public List<CarPark> getCarParks() {
        EntityManager manager = emf.createEntityManager();
        Query query = manager.createNamedQuery("findAllCarParks", CarPark.class);
        return query.getResultList();
    }

    @Override
    public CarPark updateCarPark(Object carPark) {
        if(carPark instanceof CarPark) {
            if (((CarPark) carPark).getCarParkId() == null) {
                return null;
            }
            EntityManager manager = emf.createEntityManager();
            CarPark carPark1 = manager.find(CarPark.class, ((CarPark) carPark).getCarParkId());
            if (carPark1 != null) {
                if (((CarPark) carPark).getName() != null) {
                    carPark1.setName(((CarPark) carPark).getName());
                } else {
                    manager.close();
                    return null;
                }
                carPark1.setAddress(((CarPark) carPark).getAddress());
                carPark1.setPricePerHour(((CarPark) carPark).getPricePerHour());
                carPark1.setFloors(((CarPark) carPark).getFloors());
                try {
                    manager.getTransaction().begin();
                    manager.merge(carPark1);
                    manager.getTransaction().commit();
                    manager.close();
                    return carPark1;
                }catch (Exception e){
                    return null;
                }
            }
        }
        return null;
    }

    @Override
    public CarPark deleteCarPark(Long carParkId) {
        EntityManager manager = emf.createEntityManager();
        CarPark carPark = manager.find(CarPark.class, carParkId);
        if (carPark != null) {
            carPark.getFloors().forEach(floor -> floor.getParkingSpots().forEach(parkingSpot -> parkingSpot.getReservations().forEach(reservation -> endReservation(reservation.getReservationId()))));
            carPark.getFloors().forEach(floor -> floor.getParkingSpots().forEach(parkingSpot -> parkingSpot.getReservations().forEach(reservation -> reservation.setParkingSpot(null))));
            carPark.getFloors().forEach(floor -> floor.getParkingSpots().forEach(parkingSpot -> parkingSpot.getCarType().getParkingSpots().remove(parkingSpot)));
            EntityTransaction transaction = manager.getTransaction();
            transaction.begin();
            manager.remove(carPark);
            transaction.commit();
            manager.close();
            return carPark;
        }else {
            return null;
        }
    }

    @Override
    public CarParkFloor createCarParkFloor(Long carParkId, String floorIdentifier) {
        EntityManager manager = emf.createEntityManager();
        CarPark carPark = manager.find(CarPark.class, carParkId);
        if (carPark != null){
            List<CarParkFloor> floors = carPark.getFloors();
            for (CarParkFloor floor : floors) {
                if(floor.getFloorIdentifier().equals(floorIdentifier)){
                    manager.close();
                    return null;
                }
            }
            CarParkFloor carParkFloor = new CarParkFloor();
            carParkFloor.setFloorIdentifier(floorIdentifier);
            carParkFloor.setCarPark(carPark);
            carPark.addFloor(carParkFloor);
            manager.getTransaction().begin();
            manager.persist(carParkFloor);
            manager.getTransaction().commit();
            manager.close();
            return carParkFloor;
        }
        return null;
    }

    @Override
    public CarParkFloor getCarParkFloor(Long carParkFloorId) {
        EntityManager manager = emf.createEntityManager();
        CarParkFloor carParkFloor = manager.find(CarParkFloor.class, carParkFloorId);
        manager.close();
        return carParkFloor;
    }


    public List<CarParkFloor> getCarParkFloors(Long carParkId) {
        EntityManager manager = emf.createEntityManager();
        CarPark carPark = manager.find(CarPark.class, carParkId);
        manager.close();
        return new ArrayList<>(carPark.getFloors());
    }

    @Override
    public CarParkFloor updateCarParkFloor(Object carParkFloor) {
        if (carParkFloor instanceof CarParkFloor) {
            EntityManager manager = emf.createEntityManager();
            CarParkFloor cp = manager.find(CarParkFloor.class, ((CarParkFloor) carParkFloor).getCarParkFloorId());
            if(cp != null){
                if(((CarParkFloor) carParkFloor).getFloorIdentifier().equals(cp.getFloorIdentifier())){
                    return cp;
                }
                CarPark carPark = cp.getCarPark();
                for (CarParkFloor cpf: carPark.getFloors()) {
                    if(((CarParkFloor) carParkFloor).getFloorIdentifier().equals(cpf.getFloorIdentifier())){
                        return null;
                    }
                }
                cp.setFloorIdentifier(((CarParkFloor) carParkFloor).getFloorIdentifier());
                manager.getTransaction().begin();
                manager.merge(cp);
                manager.getTransaction().commit();
                return cp;
            }
        }
        return null;
    }

    @Override
    public CarParkFloor deleteCarParkFloor(Long carParkFloorId) {
        EntityManager manager = emf.createEntityManager();
        CarParkFloor carParkFloor = manager.find(CarParkFloor.class, carParkFloorId);
        if (carParkFloor != null) {
            CarPark carPark = carParkFloor.getCarPark();
            carPark.getFloors().remove(carParkFloor);
            carParkFloor.getParkingSpots().forEach(parkingSpot -> parkingSpot.getReservations().forEach(reservation -> endReservation(reservation.getReservationId())));
            carParkFloor.getParkingSpots().forEach(parkingSpot -> parkingSpot.getReservations().forEach(reservation -> reservation.setParkingSpot(null)));
            carParkFloor.getParkingSpots().forEach(parkingSpot -> parkingSpot.getCarType().getParkingSpots().remove(parkingSpot));
            EntityTransaction transaction = manager.getTransaction();
            transaction.begin();
            manager.remove(carParkFloor);
            transaction.commit();
            manager.close();
            return carParkFloor;
        }else {
            manager.close();
            return null;
        }
    }

    @Override
    public ParkingSpot createParkingSpot(Long carParkId, String floorIdentifier, String spotIdentifier) {
        EntityManager manager = emf.createEntityManager();
        CarPark carPark = manager.find(CarPark.class, carParkId);
        if(carPark != null){
            CarParkFloor carParkFloor = carPark.getByFloorIdentifier(floorIdentifier);
            if (carParkFloor != null){
                if(spotIdentifier == null){
                    manager.close();
                    return null;
                }
                for (CarParkFloor cpf : carPark.getFloors()) {
                    for (ParkingSpot ps : cpf.getParkingSpots().stream().collect(Collectors.toList())) {
                        if (ps.getSpotIdentifier().equals(spotIdentifier)) {
                            manager.close();
                            return null;
                        }
                    }
                }
                ParkingSpot parkingSpot = new ParkingSpot();
                parkingSpot.setSpotIdentifier(spotIdentifier);
                parkingSpot.setFloor(carParkFloor);
                Object carType = createDefaultCarType();
                parkingSpot.setCarType((CarType) carType);
                ((CarType) carType).addParkingSpot(parkingSpot);
                carParkFloor.addParkingSpot(parkingSpot);
                manager.getTransaction().begin();
                manager.persist(parkingSpot);
                manager.getTransaction().commit();
                manager.getTransaction().begin();
                manager.merge(carType);
                manager.getTransaction().commit();
                manager.close();
                return parkingSpot;
            }
        }
        manager.close();
        return null;
    }

    @Override
    public ParkingSpot getParkingSpot(Long parkingSpotId) {
        EntityManager manager = emf.createEntityManager();
        ParkingSpot parkingSpot = manager.find(ParkingSpot.class, parkingSpotId);
        manager.close();
        return parkingSpot;
    }

    public List<ParkingSpot> getParkingSpots(Long carParkId, String floorIdentifier) {
        EntityManager manager = emf.createEntityManager();
        CarPark carPark = manager.find(CarPark.class, carParkId);
        if(carPark!=null) {
            CarParkFloor carParkFloor = carPark.getByFloorIdentifier(floorIdentifier);
            if(carParkFloor!= null) {
                List<ParkingSpot> parkingSpots = carParkFloor.getParkingSpots();
                manager.close();
                return parkingSpots.stream().collect(Collectors.toList());
            }
        }
        manager.close();
        return new ArrayList<>();
    }


    public Map<String, List<ParkingSpot>> getParkingSpots(Long carParkId) {
        EntityManager manager = emf.createEntityManager();
        CarPark carPark = manager.find(CarPark.class, carParkId);
        Map<String, List<ParkingSpot>> map = new HashMap<>(Collections.emptyMap());
        if (carPark != null){
            for (CarParkFloor carParkFloor: carPark.getFloors()) {
                String entryString = carParkFloor.getFloorIdentifier();
                List<ParkingSpot> entryList = new ArrayList<>();
                entryList.addAll(carParkFloor.getParkingSpots());
                map.put(entryString, entryList);
            }
        }
        manager.close();
        return map;
    }


    public Map<String, List<ParkingSpot>> getAvailableParkingSpots(String carParkName) {
        Object carPark = getCarPark(carParkName);
        if(carPark instanceof CarPark){
            Map<String, List<ParkingSpot>> map = new HashMap<>();
            List<CarParkFloor> carParkFloors = ((CarPark) carPark).getFloors();
            for (CarParkFloor carParkFloor: carParkFloors){
                List<ParkingSpot> parkingSpots = carParkFloor.getParkingSpots();
                List<ParkingSpot> parkingSpotsEntry = new ArrayList<>();
                for(ParkingSpot parkingSpot: parkingSpots){
                    if(parkingSpot.isAvailable()){
                        parkingSpotsEntry.add(parkingSpot);
                    }
                }
                map.put(carParkFloor.getFloorIdentifier(), parkingSpotsEntry);
            }
            return map;
        }
        return new HashMap<>();
    }


    public Map<String, List<ParkingSpot>> getOccupiedParkingSpots(String carParkName) {
        Object carPark = getCarPark(carParkName);
        if(carPark instanceof CarPark){
            Map<String, List<ParkingSpot>> map = new HashMap<>();
            List<CarParkFloor> carParkFloors = ((CarPark) carPark).getFloors();
            for (CarParkFloor carParkFloor: carParkFloors){
                List<ParkingSpot> parkingSpots = carParkFloor.getParkingSpots();
                List<ParkingSpot> parkingSpotsEntry = new ArrayList<>();
                for(ParkingSpot parkingSpot: parkingSpots){
                    if(parkingSpot.isOccupied()){
                        parkingSpotsEntry.add(parkingSpot);
                    }
                }
                map.put(carParkFloor.getFloorIdentifier(), parkingSpotsEntry); //nvm ci dovre
            }
            return map;
        }
        return new HashMap<>();
    }

    @Override
    public ParkingSpot updateParkingSpot(Object parkingSpot) {
        if(parkingSpot instanceof ParkingSpot){
            if(((ParkingSpot) parkingSpot).getParkingSpotId() != null){
                EntityManager manager = emf.createEntityManager();
                ParkingSpot parkingSpot1 = manager.find(ParkingSpot.class, ((ParkingSpot) parkingSpot).getParkingSpotId());
                if (parkingSpot1 != null) {
                    if (((ParkingSpot) parkingSpot).getSpotIdentifier() != null) {
                        if (!((ParkingSpot) parkingSpot).getSpotIdentifier().equals(parkingSpot1.getSpotIdentifier())) {
                            for (CarParkFloor cpf : parkingSpot1.getFloor().getCarPark().getFloors()) {
                                for (ParkingSpot ps : cpf.getParkingSpots()) {
                                    if (ps.getSpotIdentifier().equals(((ParkingSpot) parkingSpot).getSpotIdentifier())) {
                                        return null;
                                    }
                                }
                            }
                            parkingSpot1.setCarType(((ParkingSpot) parkingSpot).getCarType());
                            parkingSpot1.setSpotIdentifier(((ParkingSpot) parkingSpot).getSpotIdentifier());
                            manager.getTransaction().begin();
                            manager.merge(parkingSpot1);
                            manager.getTransaction().commit();
                            manager.close();
                            return parkingSpot1;
                        }

                    }
                }
            }
        }
        return null;
    }

    @Override
    public ParkingSpot deleteParkingSpot(Long parkingSpotId) {
        EntityManager manager = emf.createEntityManager();
        ParkingSpot parkingSpot = manager.find(ParkingSpot.class, parkingSpotId);
        if(parkingSpot!=null){
            CarParkFloor carParkFloor = parkingSpot.getFloor();
            carParkFloor.getParkingSpots().remove(parkingSpot);
            parkingSpot.getReservations().forEach(reservation -> endReservation(reservation.getReservationId()));
            parkingSpot.getReservations().forEach(reservation -> reservation.setParkingSpot(null));
            parkingSpot.getCarType().getParkingSpots().remove(parkingSpot);
            manager.getTransaction().begin();
            manager.remove(parkingSpot);
            manager.getTransaction().commit();
            manager.close();
            return parkingSpot;
        }
        manager.close();
        return null;
    }

    @Override
    public Car createCar(Long userId, String brand, String model, String colour, String vehicleRegistrationPlate) {
        if(vehicleRegistrationPlate!=null) {
            try {
                Car car = new Car();
                car.setBrand(brand);
                car.setModel(model);
                car.setColour(colour);
                car.setVehicleRegistrationPlate(vehicleRegistrationPlate);
                EntityManager manager = emf.createEntityManager();
                User user = manager.find(User.class, userId);
                if(user!=null) {
                    Object carType = getCarType("Benzin");
                    if(carType == null){
                        carType = createDefaultCarType();
                    }
                    car.setCarType((CarType) carType);
                    ((CarType) carType).addCar(car);
                    user.addCar(car);
                    car.setUser(user);
                    manager.getTransaction().begin();
                    manager.persist(car);
                    manager.getTransaction().commit();
                    manager.getTransaction().begin();
                    manager.merge(carType);
                    manager.getTransaction().commit();
                    manager.close();
                    return car;
                }
            }catch(Exception e){
                return null;
            }
        }
        return null;
    }

    public List<Car> getCars(){
        EntityManager manager = emf.createEntityManager();
        TypedQuery<Car> query = manager.createQuery("SELECT c from Car c", Car.class);
        List<Car> cars = query.getResultList();
        manager.close();
        return cars;
    }

    @Override
    public Car getCar(Long carId) {
        EntityManager manager = emf.createEntityManager();
        Car car = manager.find(Car.class, carId);
        manager.close();
        return car;
    }

    @Override
    public Car getCar(String vehicleRegistrationPlate) {
        try {
            EntityManager manager = emf.createEntityManager();
            Query query = manager.createNamedQuery("findByPlate");
            query.setParameter("plate", vehicleRegistrationPlate);
            return (Car) query.getSingleResult();
        } catch (NoResultException e){
            return null;
        }
    }


    public List<Car> getCars(Long userId) {
        EntityManager manager = emf.createEntityManager();
        User user = manager.find(User.class, userId);
        manager.close();
        if (user!= null){
            return user.getCars().stream().collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public Car updateCar(Object car) {
        if(car instanceof Car){
            if(((Car) car).getCarId() != null){
                EntityManager manager = emf.createEntityManager();
                Car car1 = manager.find(Car.class, ((Car) car).getCarId());
                if(car1!=null){
                    car1.setBrand(((Car) car).getBrand());
                    car1.setColour(((Car) car).getColour());
                    car1.setModel(((Car) car).getModel());
                    car1.setVehicleRegistrationPlate(((Car) car).getVehicleRegistrationPlate());
                    car1.setCarType(((Car) car).getCarType());
                    try{
                        manager.getTransaction().begin();
                        manager.merge(car1);
                        manager.getTransaction().commit();
                        manager.close();
                        return car1;
                    }catch (Exception e){
                        return null;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public Car deleteCar(Long carId) {
        EntityManager manager = emf.createEntityManager();
        Car car = manager.find(Car.class, carId);
        if (car != null){
            car.getUser().getCars().remove(car);
            car.getReservations().forEach(reservation -> endReservation(reservation.getReservationId()));
            car.getReservations().forEach(reservation -> reservation.setCar(null));
            car.getCarType().getCars().remove(car);
            manager.getTransaction().begin();
            manager.remove(car);
            manager.getTransaction().commit();
            manager.close();
            return car;
        }
        manager.close();
        return null;
    }

    @Override
    public User createUser(String firstname, String lastname, String email) {
        if (email != null) {
            try {
                EntityManager manager = emf.createEntityManager();
                User user = new User();
                user.setFirstname(firstname);
                user.setLastname(lastname);
                user.setEmail(email);
                manager.getTransaction().begin();
                manager.persist(user);
                manager.getTransaction().commit();
                manager.close();
                return user;
            }catch (Exception e){
                return null;
            }

        }
        return null;
    }

    @Override
    public User getUser(Long userId) {
        EntityManager manager = emf.createEntityManager();
        User user = manager.find(User.class, userId);
        manager.close();
        return user;
    }

    @Override
    public User getUser(String email) {
        if(email!=null) {
            try {
                EntityManager manager = emf.createEntityManager();
                Query query = manager.createNamedQuery("findByEmail");
                query.setParameter("email", email);
                return (User) query.getSingleResult();
            } catch (NoResultException e) {
                return null;
            }
        }
        return null;
    }


    public List<User> getUsers() {
        EntityManager manager = emf.createEntityManager();
        Query query = manager.createNamedQuery("findAllUsers");
        return query.getResultList();
    }

    @Override
    public User updateUser(Object user) {
        return null;
    }

    @Override
    public User deleteUser(Long userId) {
        EntityManager manager = emf.createEntityManager();
        User user = manager.find(User.class, userId);
        if (user != null){
            try {
                user.getCars().forEach(car -> car.getReservations().forEach(reservation -> endReservation(reservation.getReservationId())));
                user.getCars().forEach(car -> car.getReservations().forEach(reservation -> reservation.setCar(null)));
                user.getCars().forEach(car -> car.getCarType().getCars().remove(car));
                manager.getTransaction().begin();
                manager.remove(user);
                manager.getTransaction().commit();
                manager.close();
                return user;
            }catch (Exception e){
                return null;
            }
        }
        manager.close();
        return null;
    }

    @Override
    public Reservation createReservation(Long parkingSpotId, Long carId) {
        EntityManager manager = emf.createEntityManager();
        Car car = manager.find(Car.class, carId);
        if(car != null){
            if(car.getActiveReservation() == null) {
                ParkingSpot parkingSpot = manager.find(ParkingSpot.class, parkingSpotId);
                if (parkingSpot != null) {
                    if (parkingSpot.getCarType().equals(car.getCarType())) {
                        if (parkingSpot.getActiveReservation() == null) {
                            Reservation reservation = new Reservation();
                            reservation.setCar(car);
                            reservation.setParkingSpot(parkingSpot);
                            reservation.setStartDate(new Date());
                            parkingSpot.addReservation(reservation);
                            car.addReservation(reservation);
                            manager.getTransaction().begin();
                            manager.persist(reservation);
                            manager.getTransaction().commit();
                            manager.close();
                            return reservation;
                        }
                    }
                }
            }
        }
        manager.close();
        return null;
    }

    @Override
    public Reservation endReservation(Long reservationId) {
        EntityManager manager = emf.createEntityManager();
        Reservation reservation = manager.find(Reservation.class, reservationId);
        if (reservation != null){
            if(reservation.getEndDate() == null) {
                Date endDate = new Date();
                reservation.setEndDate(endDate);
                Long secs = (endDate.getTime() - reservation.getStartDate().getTime()) / 1000;
                Long hours = secs / 3600;
                if (secs > 0) {
                    hours++;
                }
                Integer hours2 = hours.intValue();
                Integer price = reservation.getParkingSpot().getFloor().getCarPark().getPricePerHour();
                Integer cost = price * hours2;
                reservation.setCost(cost);
                manager.getTransaction().begin();
                manager.persist(reservation);
                manager.getTransaction().commit();
                manager.close();
                return reservation;
            }
        }
        manager.close();
        return null;
    }


    public List<Reservation> getReservations(Long parkingSpotId, Date date) {
        EntityManager manager = emf.createEntityManager();
        ParkingSpot parkingSpot = manager.find(ParkingSpot.class, parkingSpotId);
        manager.close();
        if(parkingSpot!= null){
            List<Reservation> reservations = new ArrayList<>();
            reservations.addAll(parkingSpot.getReservations());
            List<Reservation> result = new ArrayList<>();
            for (Reservation res:reservations) {
                if(date.getDay() == res.getStartDate().getDay()){
                    result.add(res);
                }
            }
            return result;
        }
        return new ArrayList<>();
    }


    public List<Reservation> getMyReservations(Long userId) {
        EntityManager manager = emf.createEntityManager();
        User user = manager.find(User.class, userId);
        manager.close();
        if(user!=null){
            List<Reservation> myActiveReservations = new ArrayList<>();
            List<Car> cars = user.getCars();
            for(Car c: cars){
                Reservation myReservation = c.getActiveReservation();
                if(myReservation!=null) {
                    myActiveReservations.add(myReservation);
                }
            }
            return myActiveReservations.stream().collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public Reservation updateReservation(Object reservation) {
        return null;
    }

    @Override
    public CarType createCarType(String name) {
        if (name!=null) {
            try {
                EntityManager manager = emf.createEntityManager();
                CarType carType = new CarType();
                carType.setName(name);
                manager.getTransaction().begin();
                manager.persist(carType);
                manager.getTransaction().commit();
                manager.close();
                return carType;
            }catch (Exception e){
                return null;
            }
        }
        return null;
    }


    public List<CarType> getCarTypes() {
        EntityManager manager = emf.createEntityManager();
        Query query = manager.createNamedQuery("findAllCarTypes");
        return query.getResultList();
    }

    @Override
    public CarType getCarType(Long carTypeId) {
        if(carTypeId!=null){
            EntityManager manager = emf.createEntityManager();
            CarType carType = manager.find(CarType.class, carTypeId);
            manager.close();
            return carType;
        }
        return null;
    }

    @Override
    public CarType getCarType(String name) {
        if(name!=null){
            try {
                EntityManager manager = emf.createEntityManager();
                Query query = manager.createNamedQuery("findCarTypeByName");
                query.setParameter("name", name);
                return (CarType) query.getSingleResult();
            }catch (Exception e){
                return null;
            }
        }
        return null;
    }

    @Override
    public CarType deleteCarType(Long carTypeId) {
        EntityManager manager = emf.createEntityManager();
        CarType carType = manager.find(CarType.class, carTypeId);
        if(carType!=null){
            if(!carType.getParkingSpots().isEmpty()){
                return null;
            }
            if(!carType.getCars().isEmpty()){
                return null;
            }
            manager.getTransaction().begin();
            manager.remove(carType);
            manager.getTransaction().commit();
            manager.close();
            return carType;
        }
        manager.close();
        return null;
    }

    @Override
    public Car createCar(Long userId, String brand, String model, String colour, String vehicleRegistrationPlate, Long carTypeId) {
        if(vehicleRegistrationPlate!=null) {
            try {
                Car car = new Car();
                car.setBrand(brand);
                car.setModel(model);
                car.setColour(colour);
                car.setVehicleRegistrationPlate(vehicleRegistrationPlate);
                EntityManager manager = emf.createEntityManager();
                CarType carType = manager.find(CarType.class, carTypeId);
                if(carType != null) {
                    User user = manager.find(User.class, userId);
                    if (user != null) {
                        car.setCarType(carType);
                        carType.addCar(car);
                        user.addCar(car);
                        car.setUser(user);
                        manager.getTransaction().begin();
                        manager.persist(car);
                        manager.getTransaction().commit();
                        manager.close();
                        return car;
                    }
                }
            }catch(Exception e){
                return null;
            }
        }
        return null;
    }

    @Override
    public ParkingSpot createParkingSpot(Long carParkId, String floorIdentifier, String spotIdentifier, Long carTypeId) {
        EntityManager manager = emf.createEntityManager();
        CarPark carPark = manager.find(CarPark.class, carParkId);
        if(carPark != null){
            CarParkFloor carParkFloor = carPark.getByFloorIdentifier(floorIdentifier);
            if (carParkFloor != null){
                if(spotIdentifier == null){
                    manager.close();
                    return null;
                }
                for (ParkingSpot ps : carParkFloor.getParkingSpots().stream().collect(Collectors.toList())){
                    if (ps.getSpotIdentifier().equals(spotIdentifier)){
                        manager.close();
                        return null;
                    }
                }
                ParkingSpot parkingSpot = new ParkingSpot();
                parkingSpot.setSpotIdentifier(spotIdentifier);
                parkingSpot.setFloor(carParkFloor);
                CarType carType = manager.find(CarType.class, carTypeId);
                if(carType==null){
                    manager.close();
                    return null;
                }
                carParkFloor.addParkingSpot(parkingSpot);
                parkingSpot.setCarType(carType);
                carType.addParkingSpot(parkingSpot);
                manager.getTransaction().begin();
                manager.persist(parkingSpot);
                manager.getTransaction().commit();
                manager.getTransaction().begin();
                manager.merge(carType);
                manager.getTransaction().commit();
                manager.close();
                return parkingSpot;
            }
        }
        manager.close();
        return null;
    }

    public CarType createDefaultCarType(){
        if(getCarType("Benzin")!=null){
            return getCarType("Benzin");
        }
        EntityManager manager = emf.createEntityManager();
        CarType carType = new CarType();
        carType.createDefaultCarType();
        manager.getTransaction().begin();
        manager.persist(carType);
        manager.getTransaction().commit();
        manager.close();
        return carType;
    }
}
