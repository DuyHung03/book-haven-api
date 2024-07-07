package com.duyhung.bookstoreapi.controller;

import com.duyhung.bookstoreapi.entity.ApiResponse;
import com.duyhung.bookstoreapi.entity.VNPayResponse;
import com.duyhung.bookstoreapi.service.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final VNPayService vnPayService;

    @GetMapping("/vn-pay")
    public ResponseEntity<?> pay(@Valid @RequestParam long amount, HttpServletRequest request) {
        request.setAttribute("amount", amount);
        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Success",
                vnPayService.createVnPayPayment(request)
        ));
    }

    @GetMapping("/vn-pay-callback")
    public ResponseEntity<?> payCallbackHandler(HttpServletRequest request) {
        String status = request.getParameter("vnp_ResponseCode");
        if (status.equals("00")) {
            vnPayService.savePayment(request);
            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), null, new VNPayResponse("00", "Success", null)));
        } else {
            return ResponseEntity.badRequest().body(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Transaction Failed", null));
        }
    }
}
