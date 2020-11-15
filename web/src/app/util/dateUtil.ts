export function formatDateTime(stringDate: string | undefined): string {
  if (stringDate) {
    return new Date(stringDate).toLocaleDateString(navigator.language, {
      day: 'numeric',
      month: 'short',
      year: 'numeric',
      hour: 'numeric',
      minute: 'numeric',
    });
  }

  return '';
}
