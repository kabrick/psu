<?php
include 'dbconfig.php';

$id = $_GET['id'];

$responce = array();

$sql = "SELECT * FROM psu_ecpd_questions WHERE id = '$id'";

$result = $conn->query($sql);

$result_assoc = $result->fetch_assoc();

$responce['question'] = $result_assoc['question'];

$sql = "SELECT * FROM psu_ecpd_answers WHERE question_id = '$id' AND status = 1";

$result = $conn->query($sql);

$result_assoc = $result->fetch_assoc();

$responce['correct'] = $result_assoc['answer'];

$sql = "SELECT * FROM psu_ecpd_answers WHERE question_id = '$id' AND status = 0";

$counter = 1;

$result = $conn->query($sql);

if ($result->num_rows > 0) {
	while($row = $result->fetch_assoc()) {

		if ($counter == 1) {
			$responce['incorrect1'] = $row['answer'];
		} else if ($counter == 2) {
			$responce['incorrect2'] = $row['answer'];
		} else if ($counter == 3) {
			$responce['incorrect3'] = $row['answer'];
		}

		$counter++;
 	}
}

echo json_encode($responce);

?>