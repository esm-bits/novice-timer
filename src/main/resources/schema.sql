CREATE TABLE IF NOT EXISTS subjects (
	subjects_id SERIAL,
	title VARCHAR(100) ,
	minutes INT,
	idobata_user VARCHAR(30),
	agenda_id INT
);

CREATE TABLE IF NOT EXISTS agendas (
	agendas_id SERIAL
);
