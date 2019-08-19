<?php
include 'dbconfig.php';

$id = $_GET['id'];

$sql = "SELECT id, pharmacy_id, from_period, to_period, average_score AS score, timestamp FROM psu_pharmacist_assessment WHERE pharmacist_id = '$id' ORDER BY id DESC";

$arr = [];
$counter = 0;

$result = $conn->query($sql);

if ($result->num_rows > 0) {
	while($row = $result->fetch_assoc()) {
		$id = $row['pharmacy_id'];

		//get the pharmacy name
		$sql1 = "SELECT pharmacy FROM psu_pharmacies WHERE id = '$id'";

		$result1 = $conn->query($sql1);
		$result_assoc1 = $result1->fetch_assoc();

		$arr[$counter]['id'] = $row['id'];
		$arr[$counter]['from_period'] = $row['from_period'];
		$arr[$counter]['to_period'] = $row['to_period'];
		$arr[$counter]['timestamp'] = $row['timestamp'];
		$arr[$counter]['score'] = $row['score'];
		$arr[$counter]['pharmacy_name'] = $result_assoc1['pharmacy'];

		$counter++;
 	}
} else {
	//
}

echo json_encode($arr);

?>