INSERT IGNORE INTO SimDetails (simId, serviceNumber, simNumber, simStatus) VALUES
 (1, '1234567891', '1234567891234', 'active'),
 (2, '1234567892', '1234567891235', 'inactive');

INSERT IGNORE INTO SimOffers (offerId, callQty, cost, dataQty, duration, offerName, simId) VALUES
 (1, 100, 100, 120, 10, 'Free calls and data', 1),
 (2, 150, 50, 100, 15, 'Free calls', 2);

INSERT IGNORE INTO CustomerAddress (addressId, address, city, pincode, state) VALUES
 (1, 'Jayanagar', 'Bangalore', '560041', 'Karnataka'),
 (2, 'Vijaynagar', 'Mysore', '567017', 'Karna');

INSERT IGNORE INTO Customer (uniqueIdNumber, dateOfBirth, emailAddress, firstName, lastName, idType, customerAddress_addressId, simId, state) VALUES
 ('1234567891234567', '1990-12-12', 'smith@abc.com', 'Smith', 'John', 'Aadhar', 1, 1, 'Karnataka'),
 ('1234567891234568', '1998-12-12', 'bob@abc.com', 'Bob', 'Sam', 'Aadhar', 2, 2, 'Karnataka');

INSERT IGNORE INTO CustomerIdentity (uniqueIdNumber, dateOfbirth, firstName, lastName, emailAddress, state) VALUES
 ('1234567891234567', '1990-12-12', 'Smith', 'John', 'smith@abc.com', 'Karnataka'),
 ('1234567891234568', '1998-12-12', 'Bob', 'Sam', 'bob@abc.com', 'Karnataka');
