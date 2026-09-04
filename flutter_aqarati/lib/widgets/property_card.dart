import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import '../models/property.dart';
import '../theme/app_theme.dart';

class PropertyCard extends StatelessWidget {
  final Property property;
  final VoidCallback onTap;
  final VoidCallback onToggleFavorite;

  const PropertyCard({
    super.key,
    required this.property,
    required this.onTap,
    required this.onToggleFavorite,
  });

  @override
  Widget build(BuildContext context) {
    final currencyFormatter = NumberFormat('#,###', 'ar');
    final isRent = property.purpose == "للإيجار";

    return Card(
      margin: const EdgeInsets.only(bottom: 16),
      clipBehavior: Clip.antiAlias,
      child: InkWell(
        onTap: onTap,
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Stack(
              children: [
                Image.network(
                  property.imageUrl,
                  height: 210,
                  width: double.infinity,
                  fit: BoxFit.cover,
                  errorBuilder: (ctx, _, __) => Container(
                    height: 210,
                    color: AppTheme.darkSurfaceVariant,
                    child: const Center(
                      child: Icon(Icons.apartment, size: 54, color: AppTheme.goldAccent),
                    ),
                  ),
                ),
                Positioned.fill(
                  child: DecoratedBox(
                    decoration: BoxDecoration(
                      gradient: LinearGradient(
                        begin: Alignment.topCenter,
                        end: Alignment.bottomCenter,
                        colors: [
                          Colors.transparent,
                          Colors.black.withOpacity(0.85),
                        ],
                        stops: const [0.4, 1.0],
                      ),
                    ),
                  ),
                ),
                // شارة الغرض (للبيع / للإيجار)
                Positioned(
                  top: 12,
                  right: 12,
                  child: Container(
                    padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 5),
                    decoration: BoxDecoration(
                      color: isRent ? AppTheme.tagGreenBg : AppTheme.goldAccent.withOpacity(0.92),
                      borderRadius: BorderRadius.circular(8),
                      border: Border.all(
                        color: isRent ? AppTheme.tagGreen.withOpacity(0.4) : Colors.transparent,
                      ),
                    ),
                    child: Text(
                      property.purpose,
                      style: TextStyle(
                        color: isRent ? AppTheme.tagGreen : AppTheme.primaryOnColor,
                        fontWeight: FontWeight.bold,
                        fontSize: 12,
                      ),
                    ),
                  ),
                ),
                // شارة نوع العقار
                Positioned(
                  top: 12,
                  right: 80,
                  child: Container(
                    padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 5),
                    decoration: BoxDecoration(
                      color: AppTheme.darkSurface.withOpacity(0.85),
                      borderRadius: BorderRadius.circular(8),
                      border: Border.all(color: AppTheme.darkOutline),
                    ),
                    child: Text(
                      property.type,
                      style: const TextStyle(
                        color: AppTheme.textMain,
                        fontWeight: FontWeight.w600,
                        fontSize: 12,
                      ),
                    ),
                  ),
                ),
                // زر التفضيل
                Positioned(
                  top: 12,
                  left: 12,
                  child: Material(
                    color: AppTheme.darkSurface.withOpacity(0.85),
                    shape: const CircleBorder(
                      side: BorderSide(color: AppTheme.darkOutline),
                    ),
                    child: InkWell(
                      customBorder: const CircleBorder(),
                      onTap: onToggleFavorite,
                      child: Padding(
                        padding: const EdgeInsets.all(8.0),
                        child: Icon(
                          property.isFavorite ? Icons.favorite : Icons.favorite_border,
                          size: 20,
                          color: property.isFavorite ? AppTheme.favoriteRed : AppTheme.goldAccent,
                        ),
                      ),
                    ),
                  ),
                ),
                // السعر في زاوية الصورة السفلية
                Positioned(
                  bottom: 12,
                  right: 12,
                  child: Row(
                    crossAxisAlignment: CrossAxisAlignment.baseline,
                    textBaseline: TextBaseline.alphabetic,
                    children: [
                      Text(
                        currencyFormatter.format(property.price),
                        style: const TextStyle(
                          color: AppTheme.goldAccent,
                          fontSize: 22,
                          fontWeight: FontWeight.w900,
                        ),
                      ),
                      const SizedBox(width: 4),
                      Text(
                        property.currency,
                        style: TextStyle(
                          color: AppTheme.goldAccent.withOpacity(0.85),
                          fontSize: 13,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                    ],
                  ),
                ),
              ],
            ),
            Padding(
              padding: const EdgeInsets.all(16),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    property.title,
                    maxLines: 1,
                    overflow: TextOverflow.ellipsis,
                    style: const TextStyle(
                      fontSize: 16,
                      fontWeight: FontWeight.bold,
                      color: AppTheme.textMain,
                    ),
                  ),
                  const SizedBox(height: 6),
                  Row(
                    children: [
                      const Icon(Icons.location_on, size: 16, color: AppTheme.goldAccent),
                      const SizedBox(width: 4),
                      Expanded(
                        child: Text(
                          "${property.neighborhood}، ${property.city}",
                          style: const TextStyle(color: AppTheme.textSecondary, fontSize: 13),
                          overflow: TextOverflow.ellipsis,
                        ),
                      ),
                    ],
                  ),
                  const SizedBox(height: 12),
                  const Divider(height: 1, color: AppTheme.darkOutline),
                  const SizedBox(height: 12),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceAround,
                    children: [
                      _specItem(Icons.square_foot, "${property.area.toInt()} م²"),
                      if (property.bedrooms > 0)
                        _specItem(Icons.bed, "${property.bedrooms} غرف"),
                      if (property.bathrooms > 0)
                        _specItem(Icons.bathtub, "${property.bathrooms} حمامات"),
                    ],
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _specItem(IconData icon, String text) {
    return Row(
      children: [
        Icon(icon, size: 17, color: AppTheme.goldAccent),
        const SizedBox(width: 6),
        Text(
          text,
          style: const TextStyle(
            fontSize: 13,
            fontWeight: FontWeight.w600,
            color: AppTheme.textMain,
          ),
        ),
      ],
    );
  }
}
