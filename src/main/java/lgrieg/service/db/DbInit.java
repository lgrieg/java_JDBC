package lgrieg.service.db;
import lombok.AllArgsConstructor;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.stream.Collectors;
@AllArgsConstructor
public class DbInit {
    final JdbcTemplate source;

    private String getSQL(String name) throws IOException {
        try (BufferedReader br = new BufferedReader (
                new FileReader(name))) {
            return br.lines().collect(Collectors.joining("\n"));
        }
    }

    public void create(String path) throws SQLException, IOException {
        String sql = getSQL(path);
        source.statement(stmt -> {
            stmt.executeUpdate(sql);
        });
    }
}
