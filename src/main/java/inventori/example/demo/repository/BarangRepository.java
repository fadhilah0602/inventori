package inventori.example.demo.repository;

import inventori.example.demo.model.Barang;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BarangRepository extends JpaRepository<Barang, Long> {
}
