package com.Bigproject.ecommerce_springboot.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.Bigproject.ecommerce_springboot.Repository.UserRepository;
import com.Bigproject.ecommerce_springboot.entity.User;
import com.Bigproject.ecommerce_springboot.service.DashboardService;
import com.Bigproject.ecommerce_springboot.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        // Dashboard statistics using repository methods
        model.addAttribute("totalCustomers", userService.countTotalCustomers());
        model.addAttribute("totalRetailers", userService.countTotalRetailers());
        model.addAttribute("pendingRetailers", userService.countPendingRetailers());
        model.addAttribute("approvedRetailers", userService.countApprovedRetailers());
        // Add retailers list for the dashboard retailer section
        model.addAttribute("retailers", userService.getAllRetailers());

        // Dynamic statistics from DashboardService
        model.addAttribute("totalOrders", dashboardService.getTotalOrders());
        model.addAttribute("totalSales", dashboardService.getTotalSales());
        model.addAttribute("averageSales", dashboardService.getAverageOrderValue());
        model.addAttribute("totalProducts", dashboardService.getTotalProducts());
        model.addAttribute("todayOrders", dashboardService.getTodayOrders());
        model.addAttribute("todaySales", dashboardService.getTodaySales());
        model.addAttribute("recentOrders", dashboardService.getRecentOrders());
        model.addAttribute("popularProducts", dashboardService.getPopularProducts());

        // Monthly sales data for chart
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

        return "admin-dashboard";
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
        model.addAttribute("customers", userService.getAllCustomers());
        model.addAttribute("totalCustomers", userService.countCustomers());
        return "admin-customers";
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
    public String viewAllOrders() {
        return "admin-orders";
    }
}
