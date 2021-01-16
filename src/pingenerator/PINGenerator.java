package pingenerator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PINGenerator {

	private Set<String> pinSet = new HashSet<String>();
	private int newPinNum = 1000;
	private Connection connect = null;
	private Statement statement = null;
	private ResultSet resultSet = null;

	public static void main(String[] args) {
		PINGenerator pinGenerator = new PINGenerator();
		pinGenerator.openDBConnection();
		pinGenerator.getCurrentPIN();
		pinGenerator.generatePin();
		pinGenerator.savePinToDB();
	}

	private void openDBConnection() {
		String DB_URL = "jdbc:mysql://localhost/tmt?user=root";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(DB_URL);
			statement = connect.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getCurrentPIN() {
		try {
			String query = "SELECT * FROM data_id";
			resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				String pin = resultSet.getString("pin");
				pinSet.add(pin);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void generatePin() {
		String pin = "";
		int same = 0;

		for (int a = 0; a < newPinNum; a++) {
			for (int b = 0; b < 6; b++) {
				if (b == 0 || b == 1 || b == 3 || b == 4) {
					int random = (int) (Math.random() * 26);
					char character = (char) (random + 65);
					pin = pin + character;
				} else {
					int random = (int) (Math.random() * 10);
					char num = (char) (random + 48);
					pin = pin + num;
				}
			}
			if (!pinSet.add(pin)) {
				a--;
				same++;
			}
			pin = "";
		}

		System.out.println("same: " + same);
	}

	private void savePinToDB() {
		String query = "INSERT INTO `data_id` (`id`, `pin`, `status`) VALUES ";

//        int nextID = pinList.size() - newPinNum + 1;
		int nextID = 2001;
		System.out.println("total pin: " + pinSet.size());
		System.out.println("total new pin: " + newPinNum);

		String id = "B";
		List<String> newPinList = new ArrayList<>(pinSet);
		for (int i = nextID; i < newPinNum + nextID; i++) {
			if (i > 99999) {
				id = id + i;
			} else if (i > 9999) {
				id = id + "0" + i;
			} else if (i > 999) {
				id = id + "00" + i;
			} else if (i > 99) {
				id = id + "000" + i;
			} else if (i > 9) {
				id = id + "0000" + i;
			} else {
				id = id + "00000" + i;
			}
			System.out.println(id + ": " + newPinList.get(i - 1));

			query = query + "('" + id + "', '" + newPinList.get(i - 1) + "', '0'), ";

			id = "B";
		}
		try {
			query = query.substring(0, query.length() - 2);
			System.out.println(query);
//            statement.executeUpdate(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
