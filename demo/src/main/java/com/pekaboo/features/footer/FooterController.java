package com.pekaboo.features.footer;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class FooterController implements Initializable {
    @FXML
    private ImageView footerLogoImageView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Image logoFooter = new Image(getClass().getResourceAsStream("/com/pekaboo/footer/assets/logo_footer.png"));
            footerLogoImageView.setImage(logoFooter);
        } catch (Exception e) {
            System.err.println("Gagal memuat gambar footer. Pastikan file logo_footer.png ada di folder images.");
            e.printStackTrace();
        }
    }
}