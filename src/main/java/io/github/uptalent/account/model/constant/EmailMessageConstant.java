package io.github.uptalent.account.model.constant;

public class EmailMessageConstant {
    public static final String SUBJECT_RESTORE = "Your UpTalent account was temporary deleted";
    public static final String SUBJECT_VERIFY = "Your UPTalent account not activated";
    public static final String SUBJECT_CHANGE_PASSWORD = "Your UPTalent account has request to change password";
    public static final String MESSAGE_HEADER = """
                    <div style="display: grid;\040
                        flex-direction: column;\040
                        border-radius: 10px;\040
                        padding: 20px;\040
                        font-size: larger;\040
                        border: 3px solid #48bde2;\040
                        margin: 3px;">
                        <h1 style="color: #48bde2;\040
                        margin: 0;">""";
    public static final String MESSAGE_FOOTER = """
                <h4 style="margin-bottom: 0;">
                            Best regards,
                        </h4>
                        <div style="display: flex; align-items: center; gap: 5px;">
                        <img src="https://drive.google.com/uc?export=view&id=1Fx1E7h2r8ly23p5LCD5Jpc_slgUPSu2I" alt="logo" style="width:30px; object-fit: contain;"/>
                        <h4 style="margin-left: 5px;">
                            UPTALENT
                        </h4>
                        </div>
                        </div>
                """;

    public static final String MESSAGE_VERIFY = """
                        Verify your account
                        </h1>
                        <h3 style="margin: 10px 0;">
                            Dear %s,
                        </h3>
                        <p>
                            Someone created an account with your email
                        </p>
                        <p>
                            If it wasn't you, then you don't need to do anything.
                        </p>
                        <p>
                            If it you, please activate it until <b>%s</b>
                        </p>
                        <p>\040
                            Please click the button down bellow and follow the instructions.
                        </p>
                        <a href="%s" style="width: 200px; height: 50px; align-self: center;">
                        <button style="background: linear-gradient(87.27deg, rgb(156, 218, 237) 3.18%%, rgb(0, 147, 193) 63.05%%);
                        cursor: pointer;\040
                        color:white;\040
                        font-weight: bold;\040
                        width: 200px;\040
                        height: 50px;\040
                        border: none;\040
                        border-radius: 10px;">
                            Verify your account
                        </button>
                        </a>
                    """;

    public static final String MESSAGE_RESTORE = """
                    Restore your account
                        </h1>
                        <h3 style="margin: 10px 0;">
                            Dear %s,
                        </h3>
                        <p>
                            We are sorry to hear that you have deleted your account, so to restore it no later than <b>%s</b>
                        </p>
                        <p>\040
                            Please click the button down bellow and follow the instructions.
                        </p>
                        <a href="%s" style="width: 200px; height: 50px; align-self: center;">
                        <button style="background: linear-gradient(87.27deg, rgb(156, 218, 237) 3.18%%, rgb(0, 147, 193) 63.05%%);
                        cursor: pointer;\040
                        color:white;\040
                        font-weight: bold;\040
                        width: 200px;\040
                        height: 50px;\040
                        border: none;\040
                        border-radius: 10px;">
                            Restore your account
                        </button>
                        </a>
                """;

    public static final String MESSAGE_CHANGE_PASSWORD = """
                    Change password of your account
                        </h1>
                        <h3 style="margin: 10px 0;">
                            Dear %s,
                        </h3>
                        <p>
                            We received your request for changing password, so do it no later than <b>%s</b>
                        </p>
                        <p>\040
                            Please click the button down bellow and follow the instructions.
                        </p>
                        <a href="%s" style="width: 200px; height: 50px; align-self: center;">
                        <button style="background: linear-gradient(87.27deg, rgb(156, 218, 237) 3.18%%, rgb(0, 147, 193) 63.05%%);
                        cursor: pointer;\040
                        color:white;\040
                        font-weight: bold;\040
                        width: 200px;\040
                        height: 50px;\040
                        border: none;\040
                        border-radius: 10px;">
                            Change password of your account
                        </button>
                        </a>
                """;
}
