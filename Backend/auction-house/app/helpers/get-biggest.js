import Helper from '@ember/component/helper';

export default Helper.extend({
  
  compute(params) {
    let bids = params[0];
    var highestBid = 0.00;
    
    bids.forEach((item) => {
      if(item.amount > highestBid) {
        highestBid = item.amount;
      }
    });

    return highestBid;
  }
});
