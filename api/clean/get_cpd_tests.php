<?php
include 'dbconfig.php';

$sql = "SELECT * FROM psu_ecpd_exams";

$arr = [];
$counter = 0;

$result = $conn->query($sql);

if ($result->num_rows > 0) {
	while($row = $result->fetch_assoc()) {

		$id = $row['user_id'];

		$result1 = $conn->query("SELECT name FROM psu_admin WHERE psu_id = '$id'");
		$result_assoc1 = $result1->fetch_assoc();

		$cpd_id = $row['cpd_id'];

		$result2 = $conn->query("SELECT title FROM psu_ecpd WHERE id = '$cpd_id'");
		$result_assoc2 = $result2->fetch_assoc();

		$arr[$counter]['name'] = $result_assoc1['name'];
		$arr[$counter]['title'] = $result_assoc2['title'];
		$arr[$counter]['score'] = $row['score'];
		$arr[$counter]['timestamp'] = $row['timestamp'];
		$arr[$counter]['passed'] = $row['passed'];
		$arr[$counter]['id'] = $row['id'];

		$counter++;
 	}
}

echo json_encode($arr);

?>