CREATE TABLE IF NOT EXISTS subjects (
	id SERIAL,
	title VARCHAR(100) ,
	minutes INT,
	idobataUser VARCHAR(30),
	agendaId INT
);

CREATE TABLE IF NOT EXISTS agendas (
	id SERIAL
);
