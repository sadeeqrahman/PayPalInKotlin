package com.encoders.paypalinkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.paypal.checkout.PayPalCheckout
import com.paypal.checkout.approve.OnApprove
import com.paypal.checkout.cancel.OnCancel
import com.paypal.checkout.config.CheckoutConfig
import com.paypal.checkout.config.Environment
import com.paypal.checkout.config.SettingsConfig
import com.paypal.checkout.createorder.CreateOrder
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.OrderIntent
import com.paypal.checkout.createorder.UserAction
import com.paypal.checkout.error.OnError
import com.paypal.checkout.order.Amount
import com.paypal.checkout.order.AppContext
import com.paypal.checkout.order.Order
import com.paypal.checkout.order.PurchaseUnit
import com.paypal.checkout.paymentbutton.PayPalButton

class MainActivity : AppCompatActivity() {
    private lateinit var payPalButton: PayPalButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val config = CheckoutConfig(
            application = application,
            clientId = "AWp3ET9CRGLe3m4roFcwys6Gw4l346QCPYDzm3U0ZwkRfzuwwldJTIbxaM_UgnBj0X9spyKf8VAMy84B",
            environment = Environment.SANDBOX,
            returnUrl = String.format("%s://paypalpay", "com.encoders.paypalinkotlin"),
            currencyCode = CurrencyCode.USD,
            userAction = UserAction.PAY_NOW,
            settingsConfig = SettingsConfig(
                loggingEnabled = false,
                shouldFailEligibility = false
            )
        )
        PayPalCheckout.setConfig(config)

        setContentView(R.layout.activity_main)

        payPalButton = findViewById(R.id.payPalButton)

        payPalButton.setup(

            createOrder =
            CreateOrder { createOrderActions ->
                val order =
                    Order(
                        intent = OrderIntent.CAPTURE,
                        appContext = AppContext(userAction = UserAction.PAY_NOW),
                        purchaseUnitList =
                        listOf(
                            PurchaseUnit(
                                amount =
                                Amount(currencyCode = CurrencyCode.USD, value = "10.00")
                            )
                        )
                    )
                createOrderActions.create(order)
            },
            onApprove =
            OnApprove { approval ->
                approval.orderActions.capture { captureOrderResult ->
                    Log.i("CaptureOrder", "CaptureOrderResult: $captureOrderResult")
                }
            },
            onCancel = OnCancel {
                Log.d("OnCancel", "Buyer canceled the PayPal experience.")
            },
            onError = OnError { errorInfo ->
                Log.d("OnError", "Error: $errorInfo")
            }

        )


    }

}