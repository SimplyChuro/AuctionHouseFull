import { helper } from '@ember/component/helper';

export function formatBid(params) {
  var formatted = parseFloat(params, 10).toFixed(2);

  return '$' + formatted;
}

export default helper(formatBid);
