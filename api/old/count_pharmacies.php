<?php
include 'dbconfig.php';

$id = $_GET['id'];

$sql = "SELECT id, pharmacy, location_set FROM psu_pharmacies WHERE update_by = '$id'";

$result = $conn->query($sql);

$responce = array();

$counter = 1;

if ($result->num_rows > 0){
	if ($result->num_rows > 1){
		$responce['count'] = $result->num_rows;
		while($row = $result->fetch_assoc()) {
			if($counter == 1){
				$responce['first_id'] = $row['id'];
				$responce['first_name'] = $row['pharmacy'];
				$responce['location_set'] = $row['location_set'];
			} else if ($counter == 2) {
				$responce['second_id'] = $row['id'];
				$responce['second_name'] = $row['pharmacy'];
				$responce['location_set_second'] = $row['location_set'];
			}
			++$counter;
		}
	} else {
		$result_assoc = $result->fetch_assoc();
		$responce['count'] = "1";
		$responce['pharmacy'] = $result_assoc['id'];
	}
}

echo json_encode($responce);
?>