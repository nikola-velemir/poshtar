#!/bin/bash

MISC_FILE=".idea/misc.xml"

if [ ! -f "$MISC_FILE" ]; then
    echo "No .idea/misc.xml found. Make sure you have opened the project in IntelliJ first."
    exit 1
fi

if grep -q "poshtar.core.annotations" "$MISC_FILE"; then
    echo "Poshtar entry points already configured."
    exit 0
fi

if grep -q "EntryPointsManager" "$MISC_FILE"; then
    # EntryPointsManager exists, need to add items to existing list
    # Get current list size
    SIZE=$(grep -oP 'list size="\K[0-9]+' "$MISC_FILE")
    NEWSIZE=$((SIZE + 2))

    sed -i "s/list size=\"$SIZE\"/list size=\"$NEWSIZE\"/" "$MISC_FILE"
    sed -i "s|</list>|  <item index=\"$SIZE\" class=\"java.lang.String\" itemvalue=\"io.github.nikola_velemir.poshtar.core.annotations.Handler\" />\n      <item index=\"$((SIZE + 1))\" class=\"java.lang.String\" itemvalue=\"io.github.nikola_velemir.poshtar.core.annotations.Behaviour\" />\n    </list>|" "$MISC_FILE"
else
    # EntryPointsManager doesn't exist, add it before </project>
    sed -i "s|</project>|  <component name=\"EntryPointsManager\">\n    <list size=\"2\">\n      <item index=\"0\" class=\"java.lang.String\" itemvalue=\"io.github.nikola_velemir.poshtar.core.annotations.Handler\" />\n      <item index=\"1\" class=\"java.lang.String\" itemvalue=\"io.github.nikola_velemir.poshtar.core.annotations.Behaviour\" />\n    </list>\n  </component>\n</project>|" "$MISC_FILE"
fi

echo "Done! Restart IntelliJ to apply changes."
