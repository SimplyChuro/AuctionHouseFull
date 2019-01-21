import Helper from '@ember/component/helper';
import { inject as service } from '@ember/service';

export default Helper.extend({
  session: service(),

  compute(params) {
    let session = this.get('session');
    let bids = params[0];
    var checker = false;
    
    bids.forEach((item) => {
      if(item.user.get('id') == session.userID){
        checker = true;
      }
    });

    return checker;
  }
});

