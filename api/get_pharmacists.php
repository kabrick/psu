<?php
include 'dbconfig.php';

$sql = "SELECT p.id, a.name, p.pharmacy, a.psu_id
 FROM psu_admin a, psu_pharmacies p WHERE a.type = 'pharmacists' AND a.psu_id = p.update_by";

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