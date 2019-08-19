<?php
include 'dbconfig.php';

$id = $_GET['id'];

$sql = "SELECT id, from_period, to_period, average_score AS score, timestamp FROM psu_pharmacist_assessment WHERE owner_id = '$id' ORDER BY id DESC";

$result = $conn->query($sql);

if ($result->num_rows > 0) {
	while($row[] = $result->fetch_assoc()) {
 		$json = json_encode($row);
 	}
} else {
	//
}

echo $json;

?>