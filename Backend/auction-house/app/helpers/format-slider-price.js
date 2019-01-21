import { helper } from '@ember/component/helper';

export function formatSliderPrice(params) {
  var formatted = parseFloat(params, 10).toFixed(0);

  return '$' + formatted;
}

export default helper(formatSliderPrice);
