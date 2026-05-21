package com.management.ticketasset.config;

import com.management.ticketasset.model.*;
import com.management.ticketasset.repository.AssetRepository;
import com.management.ticketasset.repository.TicketRepository;
import com.management.ticketasset.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final AssetRepository assetRepository;
    private final TicketRepository ticketRepository;
    private final PasswordEncoder passwordEncoder;
    private final Random random = new Random(42); // Seed for deterministic data

    @Autowired
    public DataInitializer(UserRepository userRepository,
                           AssetRepository assetRepository,
                           TicketRepository ticketRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.assetRepository = assetRepository;
        this.ticketRepository = ticketRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== Seeding Default System Users and Data ===");

        String encodedPassword = passwordEncoder.encode("admin123");
        List<User> users = new ArrayList<>();

        // 1. Seed 20 Users
        String[] firstNames = {"John", "Sarah", "Michael", "Emma", "David", "Jessica", "James", "Laura", "Robert", "Linda",
                               "William", "Sophia", "Richard", "Olivia", "Thomas", "Isabella", "Charles", "Mia", "Joseph", "Charlotte"};
        String[] lastNames = {"Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez",
                              "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson", "Thomas", "Taylor", "Moore", "Jackson", "Martin"};
        String[] depts = {"Engineering", "HR", "Sales", "Marketing", "Finance", "IT Support", "Legal", "Operations", "Product", "Design"};
        String[] titles = {"Software Engineer", "HR Manager", "Account Executive", "Marketing Specialist", "Financial Analyst", 
                           "Support Specialist", "Legal Counsel", "Operations Manager", "Product Manager", "UX Designer"};

        for (int i = 0; i < 20; i++) {
            User user = new User();
            if (i == 0) {
                user.setUsername("admin"); // Explicit admin for frontend basic auth
            } else {
                user.setUsername((firstNames[i] + lastNames[i]).toLowerCase());
            }
            user.setPassword(encodedPassword);
            user.setEmail(firstNames[i].toLowerCase() + "." + lastNames[i].toLowerCase() + "@company.com");
            user.setRole(i == 0 || i == 5 ? Role.ROLE_ADMIN : Role.ROLE_EMPLOYEE); // 2 Admins
            
            user.setFirstName(firstNames[i]);
            user.setLastName(lastNames[i]);
            user.setPhone("555-01" + String.format("%02d", i));
            user.setDepartment(depts[i % depts.length]);
            user.setJobTitle(titles[i % titles.length]);
            user.setLocation(i % 2 == 0 ? "New York Office" : "London Office");
            user.setHireDate(LocalDate.now().minusDays(random.nextInt(1000)));
            user.setAccountStatus(i == 19 ? "Inactive" : "Active");
            user.setManagerName("Manager " + depts[i % depts.length]);
            user.setAddressLine1(100 + i + " Corporate Blvd");
            user.setCity(i % 2 == 0 ? "New York" : "London");
            user.setState(i % 2 == 0 ? "NY" : "LND");
            user.setZipCode("1000" + i);
            user.setCountry(i % 2 == 0 ? "USA" : "UK");
            user.setTimezone(i % 2 == 0 ? "EST" : "GMT");
            user.setLanguagePref("English");

            users.add(userRepository.save(user));
        }

        System.out.println("Seeded 20 Users successfully.");

        // 2. Seed 20 Assets
        List<Asset> assets = new ArrayList<>();
        String[] categories = {"Laptop", "Monitor", "Keyboard", "Mouse", "Server", "Mobile Phone", "Tablet", "Headset", "Docking Station", "Printer"};
        String[] brands = {"Apple", "Dell", "Logitech", "HP", "Lenovo", "Samsung", "Cisco", "Sony", "Microsoft", "Epson"};

        for (int i = 0; i < 20; i++) {
            Asset asset = new Asset();
            String cat = categories[i % categories.length];
            String brand = brands[i % brands.length];
            
            asset.setName(brand + " " + cat + " Pro");
            asset.setSerialNumber("SN-" + brand.toUpperCase() + "-" + 1000 + i);
            asset.setDescription("Standard issue " + cat + " for employees.");
            
            int statusInt = random.nextInt(4);
            AssetStatus status = statusInt == 0 ? AssetStatus.AVAILABLE : 
                                 statusInt == 1 ? AssetStatus.ASSIGNED : 
                                 statusInt == 2 ? AssetStatus.UNDER_REPAIR : AssetStatus.RETIRED;
            asset.setStatus(status);
            
            if (status == AssetStatus.ASSIGNED) {
                asset.setAssignedTo(users.get(random.nextInt(users.size())));
            }

            asset.setCategory(cat);
            asset.setBrand(brand);
            asset.setModel("Pro Model X" + i);
            asset.setPurchaseDate(LocalDate.now().minusDays(random.nextInt(800)));
            asset.setPurchasePrice(500.0 + (random.nextInt(200) * 10));
            asset.setVendor("TechSupply Inc.");
            asset.setWarrantyExpiryDate(LocalDate.now().plusDays(random.nextInt(800)));
            asset.setPhysicalLocation("Storage Room " + (i % 3 + 1));
            asset.setBarcode("BC-" + 90000 + i);
            asset.setIpAddress(cat.equals("Laptop") ? "192.168.1." + (100 + i) : null);
            asset.setMacAddress(cat.equals("Laptop") ? "00:1A:2B:3C:4D:" + String.format("%02d", i) : null);
            asset.setOperatingSystem(cat.equals("Laptop") ? (i % 2 == 0 ? "Windows 11" : "macOS") : "N/A");
            asset.setCpuSpecification(cat.equals("Laptop") ? "Intel Core i7" : "N/A");
            asset.setRamSize(cat.equals("Laptop") ? "16GB" : "N/A");
            asset.setStorageSize(cat.equals("Laptop") ? "512GB SSD" : "N/A");
            asset.setInvoiceNumber("INV-" + 202600 + i);
            asset.setEndOfLifeDate(LocalDate.now().plusYears(3));

            assets.add(assetRepository.save(asset));
        }

        System.out.println("Seeded 20 Assets successfully.");

        // 3. Seed 20 Tickets
        String[] ticketCategories = {"Hardware", "Software", "Access", "Network", "Other"};
        String[] issues = {"Screen flickering", "Cannot login", "VPN disconnects", "Keyboard sticky keys", "Need software license",
                           "Printer out of ink", "Forgot password", "Requesting new monitor", "System slow", "Blue screen of death"};

        for (int i = 0; i < 20; i++) {
            Ticket ticket = new Ticket();
            ticket.setTitle(issues[i % issues.length]);
            ticket.setDescription("User reported experiencing issues regarding " + issues[i % issues.length] + ". Please investigate ASAP.");
            
            ticket.setPriority(i % 4 == 0 ? TicketPriority.HIGH : i % 3 == 0 ? TicketPriority.MEDIUM : TicketPriority.LOW);
            
            TicketStatus tStatus = i % 3 == 0 ? TicketStatus.CLOSED : i % 2 == 0 ? TicketStatus.IN_PROGRESS : TicketStatus.OPEN;
            ticket.setStatus(tStatus);
            
            ticket.setCreatedBy(users.get(random.nextInt(users.size())));
            
            // Randomly attach asset
            if (random.nextBoolean()) {
                ticket.setAsset(assets.get(random.nextInt(assets.size())));
            }

            ticket.setCategory(ticketCategories[i % ticketCategories.length]);
            ticket.setSubCategory("General Support");
            
            if (tStatus == TicketStatus.CLOSED) {
                ticket.setResolutionNotes("Issue was resolved by restarting the system and updating drivers.");
                ticket.setClosedAtDate(LocalDateTime.now().minusHours(random.nextInt(48)));
                ticket.setClosedByName("IT Admin");
                ticket.setTimeSpentMinutes(random.nextInt(120) + 15);
            }
            
            ticket.setAssignedAgentName(i % 2 == 0 ? "Admin Support" : "Helpdesk Team");
            ticket.setAssignedGroup("L1 Support");
            ticket.setDueByDate(LocalDateTime.now().plusHours(48));
            ticket.setUrgency("Medium");
            ticket.setImpact("User level");
            ticket.setContactPhone("555-888-00" + String.format("%02d", i));
            ticket.setContactEmail("support_contact" + i + "@company.com");
            ticket.setCreationSource(i % 2 == 0 ? "Web Portal" : "Email");

            ticketRepository.save(ticket);
        }

        System.out.println("Seeded 20 Tickets successfully.");
        System.out.println("=== Database Seeding Completed Successfully ===");
    }
}
