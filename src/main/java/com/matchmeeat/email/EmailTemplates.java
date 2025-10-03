package com.matchmeeat.email;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Every '%' sign has to be escaped by adding another '%',
 * otherwise it's interpreted as invalid format specifier, resulting in "Exception: Conversion = ..."
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EmailTemplates {

    public static String verificationEmailBody() {
        return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="utf-8">
                <meta name="viewport" content="width=device-width">
            </head>
            <body style="
                margin: 0;
                padding: 0;
                background-color: #f6f8fb;
                font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial !important;
                color: #0b2545 !important;
                line-height: 1.5 !important;
                font-size: 15px !important;
                -webkit-text-size-adjust: 100%%
            ">
                <!-- Preheader: short summary shown in inbox preview -->
                <div style="display: none !important; visibility: hidden; opacity: 0; color: transparent; height: 0; width: 0">
                    Just one click to finish setting up your Match Me Eat account.
                </div>
                       
                <table role="presentation" width="100%%" cellpadding="0" cellspacing="0" style="background-color: #f6f8fb; width: 100%%">
                    <tr>
                        <td align="center">
                            <table role="presentation" width="100%%" cellpadding="0" cellspacing="0" style="
                                max-width: 620px;
                                margin: 24px auto;
                                background: #ffffff;
                                border-radius: 8px;
                                border: 1px solid #e6eef8;
                                box-shadow: 0 6px 18px rgba(11, 37, 69, 0.06);
                                overflow: hidden
                            ">
                                <tr>
                                    <td style="padding: 28px">
                       
                                        <!-- Header Table -->
                                        <table role="presentation" width="100%%" cellpadding="0" cellspacing="0" style="margin-bottom: 18px">
                                            <tr>
                                                <td width="48" valign="middle" align="center">
                                                    <table role="presentation" cellpadding="0" cellspacing="0" style="border-radius: 10px; overflow: hidden">
                                                        <tr>
                                                            <td align="center" valign="middle" style="
                                                                width: 48px;
                                                                height: 48px;
                                                                background: linear-gradient(180deg, #0077d9, #004b9b);
                                                                color: #fff;
                                                                font-weight: 700;
                                                                text-align: center;
                                                            ">
                                                            <!--  Match Me Eat acronym -->
                                                                MME
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                                <td style="padding-left: 12px; vertical-align: middle">
                                                    <h1 style="margin: 0 0 8px 0; font-size: 20px; line-height: 28px; color: #0b2545">
                                                        Hey %s — welcome to Match Me Eat!
                                                    </h1>
                                                    <div style="color: #6b7a8a; font-size: 13px">
                                                        Let's confirm your email, so you can get going.
                                                    </div>
                                                </td>
                                            </tr>
                                        </table>
                       
                                        <p style="margin: 0 0 16px 0; color: #0b2545">
                                            You're one step away from signing up. Click the button below to verify your email address. It's quick — promise.
                                        </p>
                       
                                        <!-- Button -->
                                        <table role="presentation" width="100%%" cellpadding="0" cellspacing="0" style="margin: 20px 0; text-align: center">
                                            <tr>
                                                <td align="center">
                                                    <a href="%s" target="_blank" rel="noopener noreferrer" style="
                                                        display: inline-block;
                                                        padding: 12px 20px;
                                                        background-color: #0b82ff;
                                                        color: #ffffff;
                                                        text-decoration: none;
                                                        font-weight: 600;
                                                        border-radius: 8px
                                                    ">
                                                        Verify e-mail
                                                    </a>
                                                </td>
                                            </tr>
                                        </table>
                       
                                        <p style="margin: 0 0 16px 0; font-size: 13px; color: #6b7a8a; text-align: center">
                                            Link expires in %s minutes.
                                        </p>
                       
                                        <hr style="border: none; border-top: 1px solid #eef6ff; margin: 22px 0">
                       
                                        <p style="margin: 16px 0; color: #0b2545">
                                            If that wasn’t you, ignore this email — nothing will happen.
                                            If the link expires, request a new one from the application login screen.
                                        </p>
                       
                                        <p style="margin: 0; color: #0b2545">
                                            Cheers,<br>
                                            the Match Me Eat developer
                                        </p>
                       
                                    </td>
                                </tr>
                       
                                <!-- Footer -->
                                <tr>
                                    <td style="
                                        padding: 10px 0;
                                        background: #fbfdff;
                                        border-top: 1px solid #eef6ff;
                                        font-size: 12px;
                                        color: #6b7a8a;
                                        text-align: center
                                    ">
                                        <div style="max-width: 520px; margin: 0 auto">
                                            <div style="margin-bottom: 8px">Please don't respond to this e-mail.</div>
                                            <div style="color: #98a4b2">Match Me Eat</div>
                                        </div>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </body>
            </html>
            """;
    }
}
