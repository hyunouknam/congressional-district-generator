import { Component } from '@angular/core';

import { User } from '../models/user';
import { ServerCommService } from '../servercomm.service';

@Component({
  selector: 'app-current-user',
  template: `
    <div>Logged in as: <strong>{{text}}</strong></div>
`
})

export class CurrentUserComponent {
  text = "X";

  constructor(servercomm: ServerCommService) {
    let userP: Promise<User|null> = servercomm.getCurrentUserPromise();
    userP.then(user => {
      this.text = (user == null) ? "UNDEF" : user.name;
    })
  }
}

