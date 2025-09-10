import { Component } from '@angular/core';
import { HeaderComponent } from "../_commons/header/header.component";
import { FooterComponent } from "../_commons/footer/footer.component";

@Component({
  selector: 'app-demo',
  imports: [HeaderComponent, FooterComponent],
  templateUrl: './demo.component.html',
  styleUrl: './demo.component.css'
})
export class DemoComponent {

}
