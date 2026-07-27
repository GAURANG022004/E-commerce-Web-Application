package com.Bigproject.ecommerce_springboot.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.Bigproject.ecommerce_springboot.entity.Order;
import com.Bigproject.ecommerce_springboot.entity.User;
import com.Bigproject.ecommerce_springboot.service.DashboardService;
import com.Bigproject.ecommerce_springboot.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        try {
            // Dashboard statistics using repository methods
            model.addAttribute("totalCustomers", userService.countTotalCustomers());
            model.addAttribute("totalRetailers", userService.countTotalRetailers());
            model.addAttribute("pendingRetailers", userService.countPendingRetailers());
            model.addAttribute("approvedRetailers", userService.countApprovedRetailers());

            // Add retailers list for the dashboard retailer section
            model.addAttribute("retailers", userService.getAllRetailers());

            // Dynamic statistics from DashboardService with null checks
            try {
            	
                model.addAttribute("totalOrders", dashboardService.getTotalOrders());
                model.addAttribute("totalSales", dashboardService.getTotalSales());
                model.addAttribute("averageSales", dashboardService.getAverageOrderValue());
                model.addAttribute("totalProducts", dashboardService.getTotalProducts());
                model.addAttribute("todayOrders", dashboardService.getTodayOrders());
                model.addAttribute("todaySales", dashboardService.getTodaySales());
                model.addAttribute("recentOrders", dashboardService.getRecentOrders());
                model.addAttribute("popularProducts", dashboardService.getPopularProducts());
                
            } catch (Exception e) {
                // Set default values if dashboard service fails
                model.addAttribute("totalOrders", 0L);
                model.addAttribute("totalSales", 0.0);
                model.addAttribute("averageSales", 0.0);
                model.addAttribute("totalProducts", 0L);
                model.addAttribute("todayOrders", 0L);
                model.addAttribute("todaySales", 0.0);
                model.addAttribute("recentOrders", new ArrayList<>());
                model.addAttribute("popularProducts", new ArrayList<>());
            }

            // Monthly sales data for chart with null checks
            try {
                Map<String, Double> monthlySales = dashboardService.getMonthlySalesData();
                model.addAttribute("janSales", monthlySales.get("Jan"));
                model.addAttribute("febSales", monthlySales.get("Feb"));
                model.addAttribute("marSales", monthlySales.get("Mar"));
                model.addAttribute("aprSales", monthlySales.get("Apr"));
                model.addAttribute("maySales", monthlySales.get("May"));
                model.addAttribute("junSales", monthlySales.get("Jun"));
                model.addAttribute("julSales", monthlySales.get("Jul"));
                model.addAttribute("augSales", monthlySales.get("Aug"));
                model.addAttribute("sepSales", monthlySales.get("Sep"));
                model.addAttribute("octSales", monthlySales.get("Oct"));
                model.addAttribute("novSales", monthlySales.get("Nov"));
                model.addAttribute("decSales", monthlySales.get("Dec"));
            } catch (Exception e) {
                // Set default monthly sales values
                for (String month : new String[]{"janSales", "febSales", "marSales", "aprSales", "maySales", 
                                               "junSales", "julSales", "augSales", "sepSales", "octSales", 
                                               "novSales", "decSales"}) {
                    model.addAttribute(month, 0.0);
                }
            }

            return "admin-dashboard-simple";
        } catch (Exception e) {
            // If everything fails, return dashboard with minimal data
            model.addAttribute("totalCustomers", 0);
            model.addAttribute("totalRetailers", 0);
            model.addAttribute("pendingRetailers", 0);
            model.addAttribute("approvedRetailers", 0);
            model.addAttribute("retailers", new ArrayList<>());
            return "admin-dashboard-simple";
        }
    }
    
    // Manage pending retailers
    @GetMapping("/retailers/pending")
    public String viewPendingRetailers(Model model) {
        model.addAttribute("pendingRetailers", userService.getPendingRetailers());
        return "admin-pending-retailers";
    }
    
    @PostMapping("/retailers/approve/{id}")
    public String approveRetailer(@PathVariable Long id) {
        userService.approveRetailer(id);
        return "redirect:/admin/retailers/pending";
    }
    
    @PostMapping("/retailers/reject/{id}")
    public String rejectRetailer(@PathVariable Long id) {
        userService.rejectRetailer(id);
        return "redirect:/admin/retailers/pending";
    }
    
    // Admin creates approved retailer directly
    @GetMapping("/retailers/create")
    public String createRetailerForm(Model model) {
        model.addAttribute("user", new User());
        return "admin-create-retailer";
    }
    
    @PostMapping("/retailers/create")
    public String createApprovedRetailer(@ModelAttribute User user) {
        userService.createApprovedRetailer(user);
        return "redirect:/admin/retailers/pending";
    }
    
    // Manage all customers
    @GetMapping("/customers")
    public String viewAllCustomers(Model model) {
        try {
            List<User> customers = userService.getAllCustomers();
            model.addAttribute("customers", customers != null ? customers : new ArrayList<>());
            model.addAttribute("totalCustomers", userService.countCustomers());
            return "admin-customers";
        } catch (Exception e) {
            model.addAttribute("customers", new ArrayList<>());
            model.addAttribute("totalCustomers", 0);
            return "admin-customers";
        }
    }
    
    // Manage all users
    @GetMapping("/users")
    public String viewAllUsers(Model model) {
        model.addAttribute("users", userService.getAllRetailers());
        model.addAttribute("approvedRetailers", userService.getApprovedRetailers());
        model.addAttribute("rejectedRetailers", userService.getRejectedRetailers());
        return "admin-users";
    }
    
    @GetMapping("/products")
    public String manageProducts() {
        return "redirect:/products";
    }
    
    @GetMapping("/orders")
    public String viewAllOrders(Model model) {
        System.out.println("=== DEBUG: viewAllOrders method called ===");
        try {
            System.out.println("DEBUG: Calling dashboardService.getTotalOrders()");
            Long totalOrders = dashboardService.getTotalOrders();
            System.out.println("DEBUG: totalOrders = " + totalOrders);
            
            System.out.println("DEBUG: Calling dashboardService.getRecentOrders()");
            List<Order> recentOrders = dashboardService.getRecentOrders();
            System.out.println("DEBUG: recentOrders size = " + (recentOrders != null ? recentOrders.size() : 0));
            
            System.out.println("DEBUG: Calling userService.countTotalCustomers()");
            Long totalCustomers = userService.countTotalCustomers();
            System.out.println("DEBUG: totalCustomers = " + totalCustomers);
            
            System.out.println("DEBUG: Calling userService.countPendingRetailers()");
            Long pendingRetailers = userService.countPendingRetailers();
            System.out.println("DEBUG: pendingRetailers = " + pendingRetailers);
            
            model.addAttribute("totalOrders", totalOrders);
            model.addAttribute("recentOrders", recentOrders);
            model.addAttribute("totalCustomers", totalCustomers);
            model.addAttribute("pendingRetailers", pendingRetailers);
            
            System.out.println("DEBUG: Model attributes added successfully, returning admin-orders");
        } catch (Exception e) {
            System.out.println("ERROR: Exception in viewAllOrders: " + e.getMessage());
            e.printStackTrace();
            // Set default values if service fails
            model.addAttribute("totalOrders", 0L);
            model.addAttribute("recentOrders", new ArrayList<>());
            model.addAttribute("totalCustomers", 0);
            model.addAttribute("pendingRetailers", 0);
            System.out.println("DEBUG: Set default values, returning admin-orders");
        }
        return "admin-orders";
    }
}
