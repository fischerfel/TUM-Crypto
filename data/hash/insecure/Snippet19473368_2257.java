@Override
    public void registerCustomer(String customerId,String name,String password,String addr,String phone,String email) throws EntityExistsException{
    System.out.println("register Custoemr begins");        
    customerEntity= em.find(CustomerEntity.class, customerId);
    if (customerEntity != null){ 
        System.out.println("customer already exists!");
        throw new EntityExistsException();
    }
    customerEntity=new CustomerEntity();
    System.out.println("new customer created");
    String hashword = null;
    try {
    MessageDigest md5 = MessageDigest.getInstance("MD5");
    md5.update(password.getBytes());
    BigInteger hash = new BigInteger(1, md5.digest());
    hashword = hash.toString(16);
    } catch (NoSuchAlgorithmException nsae) {
    }
    System.out.println("info read is id "+customerId+" name:"+name+" password:"+password+" addr:"+addr+" phone:"+phone+" email:"+email);
    customerEntity.create(customerId, name,hashword);
    ContactEntity contact=new ContactEntity(); 
    contact.create(addr, phone, email);
    System.out.println("contact created");
    PointEntity point=new PointEntity();
    point.create(0);
    System.out.println("point created");
    customerEntity.setContact(contact);
    customerEntity.setPoint(point);
    System.out.println("set contact and point");
    System.out.println("create succesful");
    em.persist(customerEntity);
    System.out.println("after persist");
    }
