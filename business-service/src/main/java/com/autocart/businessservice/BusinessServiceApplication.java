package com.autocart.businessservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.autocart.businessservice.entity.VariantColour;
import com.autocart.businessservice.repository.VariantColourRepository;
import java.util.List;
import java.util.Arrays;

@SpringBootApplication
public class BusinessServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BusinessServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner initVariantColours(VariantColourRepository colourRepository) {
		return args -> {
			if (colourRepository.count() == 0) {
				List<VariantColour> colours = Arrays.asList(
					new VariantColour(null, 1L, "Pearl Arctic White", "#FFFFFF"),
					new VariantColour(null, 1L, "Splendid Silver", "#C0C0C0"),
					new VariantColour(null, 1L, "Sizzling Red", "#D32F2F"),
					new VariantColour(null, 2L, "Pearl Arctic White", "#FFFFFF"),
					new VariantColour(null, 2L, "Luster Blue", "#1976D2"),
					new VariantColour(null, 2L, "Novel Orange", "#F57C00"),
					new VariantColour(null, 3L, "Magma Grey", "#616161"),
					new VariantColour(null, 3L, "Midnight Black", "#212121"),
					new VariantColour(null, 4L, "Sizzling Red", "#D32F2F"),
					new VariantColour(null, 4L, "Pearl Arctic White", "#FFFFFF"),
					new VariantColour(null, 5L, "Luster Blue", "#1976D2"),
					new VariantColour(null, 5L, "Splendid Silver", "#C0C0C0"),
					new VariantColour(null, 6L, "Magma Grey", "#616161"),
					new VariantColour(null, 6L, "Pearl Arctic White", "#FFFFFF"),
					new VariantColour(null, 7L, "Exuberant Blue", "#0D47A1"),
					new VariantColour(null, 7L, "Brave Khaki", "#8D6E63"),
					new VariantColour(null, 8L, "Atlas White", "#F5F5F5"),
					new VariantColour(null, 8L, "Abyss Black", "#1A1A1A"),
					new VariantColour(null, 9L, "Ranger Khaki", "#4E5340"),
					new VariantColour(null, 9L, "Titan Grey", "#546E7A")
				);
				colourRepository.saveAll(colours);
				System.out.println("✅ Automatically seeded variant_colours table with default vehicle options!");
			}
		};
	}
}
